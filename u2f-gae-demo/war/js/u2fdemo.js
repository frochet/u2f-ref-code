// Copyright 2014 Google Inc. All rights reserved.
//
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file or at
// https://developers.google.com/open-source/licenses/bsd

///////////////////////////////////////////////////////////////////////////////
// UI Functions                                                              //
///////////////////////////////////////////////////////////////////////////////

function addTokenInfoToPage(token) {
    console.log(token);
    document
        .getElementById('tokens')
        .appendChild(tokenToDom(token));
}

function tokenToDom(token) {
  var timeString = new Date(token.enrollment_time).toLocaleString();

  var template = document.getElementById('cardTemplate');
  var card = template.content.cloneNode(true);
  card.querySelector('.card').setAttribute("id", token.public_key);
  card.querySelector('.issuer').textContent = token.issuer;
  card.querySelector('.enrollmentTimeValue').textContent = timeString;
  if (token.transports == null || token.transports === undefined) {
	  card.querySelector('.transportsValue').textContent = "None specified";
  } else {
	  card.querySelector('.transportsValue').textContent = token.transports;
  }
  card.querySelector('.keyHandle').textContent = token.key_handle;
  card.querySelector('.publicKey').textContent = token.public_key;

  $(card.querySelector('.removeCardButton'))
    .button()
    .click(function() {
      sendRemoveTokenRequest(token.public_key);
     });

  return card;
}

function removeTokenInfoFromPage(publicKey) {
  console.log(publicKey);
  $("#" + publicKey).remove();
}

function showError(message) {
  hideMessage();
  console.log(message);
  $('#errormessage').empty();
  $('#errormessage').text(message);
  $('#errorbox').addClass('visible').removeClass('invisible');
} 

function hideError() {
  $('#errorbox').addClass('invisible').removeClass('visible');
}

function showMessage(message) {
  console.log(message);
  $('#infomessage').empty();
  $('#infomessage').text(message);
  $('#infobox').addClass('visible').removeClass('invisible');
} 

function hideMessage() {
  $('#infobox').addClass('invisible').removeClass('visible');
} 

function highlightTokenCardOnPage(token) {
  console.log(token);

  var cardChildren = document.getElementById(token.public_key).children;
  for (i = 0; i < cardChildren.length; i++) {
    if ($(cardChildren[i]).hasClass("cardContent")) {
      $(cardChildren[i]).addClass("highlight");
    }
  }

  window.setTimeout(
    function() {
      for (i = 0; i < cardChildren.length; i++) {
        $(cardChildren[i]).removeClass("highlight", 2000);
      }
    },
    500
  );
}


///////////////////////////////////////////////////////////////////////////////
// AJAX Calls                                                                //
///////////////////////////////////////////////////////////////////////////////

function fetchAllUserTokens(callback) {
  $.post('/GetTokens', {}, null, 'json')
   .done(function(tokens) {
     for (var index in tokens) {
       addTokenInfoToPage(tokens[index]);
     }
   });
}

function sendRemoveTokenRequest(publicKey) {
  $.post('/RemoveToken', {
      'public_key' : publicKey
    }, null, 'json')
    .done(function(e) {
      removeTokenInfoFromPage(publicKey);
    })
    .fail(function(xhr, status) {
      showError("couldn't remove token: " + status);
    });
}

function sendBeginEnrollRequest() {
  $.post('/BeginEnroll', {
        'reregistration' : document.querySelector('#reregistration').checked
      }, null, 'json')
   .done(function(beginEnrollResponse) {
      console.log(beginEnrollResponse);
      showMessage("please touch the token");
      u2f.register(
        beginEnrollResponse.appId,
        [beginEnrollResponse.registerRequests],
        beginEnrollResponse.registeredKeys,
        function (response) {
          if (response.errorCode) {
            onError(response.errorCode, true);
          } else {
            response['sessionId'] = beginEnrollResponse.sessionId;
            onTokenEnrollSuccess(response);
          }
        });
    });
}

function sendBeginSignRequest() {
  $.post('/BeginSign', {}, null, 'json')
   .done(function(signResponse) {
      console.log(signResponse);
      var registeredKeys = signResponse.registeredKeys;
      showMessage("please touch the token");
      // Store sessionIds
      var sessionIds = {};
      for (var i = 0; i < registeredKeys.length; i++) {
        sessionIds[registeredKeys[i].keyHandle] = registeredKeys[i].sessionId;
        delete registeredKeys[i]['sessionId'];
      }
      u2f.sign(signResponse.appId, signResponse.challenge, registeredKeys, function (response) {
          if (response.errorCode) {
            onError(response.errorCode, false);
          } else {
            response['sessionId'] = sessionIds[response.keyHandle];
            onTokenSignSuccess(response);
          }
      })
   }) 
   .fail(function(xhr, status) {
      showError("can't authenticate: " + status);
   });
}

///////////////////////////////////////////////////////////////////////////////
// U2F Token Callbacks                                                       //
///////////////////////////////////////////////////////////////////////////////

function onTokenEnrollSuccess(finishEnrollData) {
  hideMessage();
  console.log(finishEnrollData);
  $.post('/FinishEnroll', finishEnrollData, null, 'json')
   .done(addTokenInfoToPage)
   .fail(function(xhr, status) { 
      showError(status); 
   });
}

function onTokenSignSuccess(responseData) {
  console.log(responseData);
  hideMessage();
  $.post('/FinishSign', responseData, null, 'json')
    .done(highlightTokenCardOnPage)
    .fail(function(xhr, status) {
      showError(status);
    });
}

function onError(code, enrolling) {
  switch (code) {
  case u2f.ErrorCodes.OTHER_ERROR:
    showError('sign error (other)');
    break;
  case u2f.ErrorCodes.BAD_REQUEST:
    showError('bad request');
    break;
  case u2f.ErrorCodes.CONFIGURATION_UNSUPPORTED:
    showError('configuration unsupported');
    break;
  case u2f.ErrorCodes.DEVICE_INELIGIBLE:
    if (enrolling)
      showError('U2F token is already registered');
    else
      showError('U2F token is not registered');
    break;
  case u2f.ErrorCodes.TIMEOUT:
    showError('timeout');
    break;
  default:
    showError('unknown error code=' + code);
    break;
  }
}

if ($.inArray(navigator.platform, ["iPhone", "iPad", "iPod"]) > -1) {
  function executeRequest (request) {
    var str = JSON.stringify(request);
    var url = "u2f://auth?" + encodeURI(str);
    location.replace(url);
  }

  u2f.callbackMap_ = {};
  u2f.sign = function(signRequests, callback, opt_timeoutSeconds) {
    var reqId = ++u2f.reqCounter_;
    u2f.callbackMap_[reqId] = callback;
    var req = {
      type: u2f.MessageTypes.U2F_SIGN_REQUEST,
      signRequests: signRequests,
      timeoutSeconds: (typeof opt_timeoutSeconds !== 'undefined' ?
          opt_timeoutSeconds : u2f.EXTENSION_TIMEOUT_SEC),
      requestId: reqId
    };
    executeRequest(req);    
  };

  u2f.register = function(registerRequests, signRequests,
    callback, opt_timeoutSeconds) {
    var reqId = ++u2f.reqCounter_;
    u2f.callbackMap_[reqId] = callback;
    var req = {
      type: u2f.MessageTypes.U2F_REGISTER_REQUEST,
      signRequests: signRequests,
      registerRequests: registerRequests,
      timeoutSeconds: (typeof opt_timeoutSeconds !== 'undefined' ?
          opt_timeoutSeconds : u2f.EXTENSION_TIMEOUT_SEC),
      requestId: reqId
    };
    executeRequest(req);
  };
}
