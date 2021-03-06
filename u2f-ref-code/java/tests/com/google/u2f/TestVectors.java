// Copyright 2014 Google Inc. All rights reserved.
//
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file or at
// https://developers.google.com/open-source/licenses/bsd

package com.google.u2f;

import static com.google.u2f.TestUtils.computeSha256;
import static com.google.u2f.TestUtils.parseCertificate;
import static com.google.u2f.TestUtils.parseCertificateBase64;
import static com.google.u2f.TestUtils.parseHex;
import static com.google.u2f.TestUtils.parsePrivateKey;
import static com.google.u2f.TestUtils.parsePublicKey;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TestVectors {
  //Test vectors from FIDO U2F: Raw Message Formats - Draft 4
  protected static final int COUNTER_VALUE = 1;
  protected static final String ACCOUNT_NAME = "test@example.com";
  protected static final Set<String> TRUSTED_DOMAINS = ImmutableSet.of("http://example.com");
  protected static final String SESSION_ID = "session_id";
  protected static final String APP_ID_ENROLL = "http://example.com";
  protected static final byte[] APP_ID_ENROLL_SHA256 = computeSha256(APP_ID_ENROLL);
  protected static final String APP_ID_SIGN = "https://gstatic.com/securitykey/a/example.com";
  protected static final byte[] APP_ID_SIGN_SHA256 = computeSha256(APP_ID_SIGN);
  protected static final String ORIGIN = "http://example.com";
  protected static final String SERVER_CHALLENGE_ENROLL_BASE64 =
      "vqrS6WXDe1JUs5_c3i4-LkKIHRr-3XVb3azuA5TifHo";
  protected static final byte[] SERVER_CHALLENGE_ENROLL = Base64
      .decodeBase64(SERVER_CHALLENGE_ENROLL_BASE64);
  protected static final String SERVER_CHALLENGE_SIGN_BASE64 = "opsXqUifDriAAmWclinfbS0e-USY0CgyJHe_Otd7z8o";
  protected static final byte[] SERVER_CHALLENGE_SIGN = Base64
      .decodeBase64(SERVER_CHALLENGE_SIGN_BASE64);
  protected static final String VENDOR_CERTIFICATE_HEX =
      "3082013c3081e4a003020102020a47901280001155957352300a06082a8648ce"
          + "3d0403023017311530130603550403130c476e756262792050696c6f74301e17"
          + "0d3132303831343138323933325a170d3133303831343138323933325a303131"
          + "2f302d0603550403132650696c6f74476e756262792d302e342e312d34373930"
          + "313238303030313135353935373335323059301306072a8648ce3d020106082a"
          + "8648ce3d030107034200048d617e65c9508e64bcc5673ac82a6799da3c144668"
          + "2c258c463fffdf58dfd2fa3e6c378b53d795c4a4dffb4199edd7862f23abaf02"
          + "03b4b8911ba0569994e101300a06082a8648ce3d0403020347003044022060cd"
          + "b6061e9c22262d1aac1d96d8c70829b2366531dda268832cb836bcd30dfa0220"
          + "631b1459f09e6330055722c8d89b7f48883b9089b88d60d1d9795902b30410df";
  protected static final X509Certificate VENDOR_CERTIFICATE =
      parseCertificate(VENDOR_CERTIFICATE_HEX);
  protected static final PrivateKey VENDOR_CERTIFICATE_PRIVATE_KEY = parsePrivateKey(
      "f3fccc0d00d8031954f90864d43c247f4bf5f0665c6b50cc17749a27d1cf7664");
  protected static final String CHANNEL_ID_STRING =
      "{"
          + "\"kty\":\"EC\","
          + "\"crv\":\"P-256\","
          + "\"x\":\"HzQwlfXX7Q4S5MtCCnZUNBw3RMzPO9tOyWjBqRl4tJ8\","
          + "\"y\":\"XVguGFLIZx1fXg3wNqfdbn75hi4-_7-BxhMljw42Ht4\""
          + "}";
  protected static final JsonObject CHANNEL_ID_JSON = (JsonObject) new JsonParser()
  .parse(CHANNEL_ID_STRING);
  protected static final String BROWSER_DATA_ENROLL = String.format(
      "{"
          + "\"typ\":\"navigator.id.finishEnrollment\","
          + "\"challenge\":\"%s\","
          + "\"cid_pubkey\":%s,"
          + "\"origin\":\"%s\"}",
          SERVER_CHALLENGE_ENROLL_BASE64,
          CHANNEL_ID_STRING,
          ORIGIN);
  protected static final String BROWSER_DATA_ENROLL_BASE64 = Base64
      .encodeBase64URLSafeString(BROWSER_DATA_ENROLL.getBytes());
  protected static final byte[] BROWSER_DATA_ENROLL_SHA256 = computeSha256(BROWSER_DATA_ENROLL
      .getBytes());
  protected static final String BROWSER_DATA_SIGN = String.format(
      "{"
          + "\"typ\":\"navigator.id.getAssertion\","
          + "\"challenge\":\"%s\","
          + "\"cid_pubkey\":%s,"
          + "\"origin\":\"%s\"}",
          SERVER_CHALLENGE_SIGN_BASE64,
          CHANNEL_ID_STRING,
          ORIGIN);
  protected static final String BROWSER_DATA_SIGN_BASE64 = Base64
      .encodeBase64URLSafeString(BROWSER_DATA_SIGN.getBytes());
  protected static final byte[] BROWSER_DATA_SIGN_SHA256 = parseHex(
      "ccd6ee2e47baef244d49a222db496bad0ef5b6f93aa7cc4d30c4821b3b9dbc57");
  protected static final byte[] REGISTRATION_REQUEST_DATA = parseHex(
      "4142d21c00d94ffb9d504ada8f99b721f4b191ae4e37ca0140f696b6983cfacb"
          + "f0e6a6a97042a4f1f1c87f5f7d44315b2d852c2df5c7991cc66241bf7072d1c4");
  protected static final byte[] REGISTRATION_RESPONSE_DATA = parseHex(
      "0504b174bc49c7ca254b70d2e5c207cee9cf174820ebd77ea3c65508c26da51b"
          + "657c1cc6b952f8621697936482da0a6d3d3826a59095daf6cd7c03e2e60385d2"
          + "f6d9402a552dfdb7477ed65fd84133f86196010b2215b57da75d315b7b9e8fe2"
          + "e3925a6019551bab61d16591659cbaf00b4950f7abfe6660e2e006f76868b772"
          + "d70c253082013c3081e4a003020102020a47901280001155957352300a06082a"
          + "8648ce3d0403023017311530130603550403130c476e756262792050696c6f74"
          + "301e170d3132303831343138323933325a170d3133303831343138323933325a"
          + "3031312f302d0603550403132650696c6f74476e756262792d302e342e312d34"
          + "373930313238303030313135353935373335323059301306072a8648ce3d0201"
          + "06082a8648ce3d030107034200048d617e65c9508e64bcc5673ac82a6799da3c"
          + "1446682c258c463fffdf58dfd2fa3e6c378b53d795c4a4dffb4199edd7862f23"
          + "abaf0203b4b8911ba0569994e101300a06082a8648ce3d040302034700304402"
          + "2060cdb6061e9c22262d1aac1d96d8c70829b2366531dda268832cb836bcd30d"
          + "fa0220631b1459f09e6330055722c8d89b7f48883b9089b88d60d1d9795902b3"
          + "0410df304502201471899bcc3987e62e8202c9b39c33c19033f7340352dba80f"
          + "cab017db9230e402210082677d673d891933ade6f617e5dbde2e247e70423fd5"
          + "ad7804a6d3d3961ef871");
  protected static final String REGISTRATION_DATA_BASE64 = Base64
      .encodeBase64URLSafeString(REGISTRATION_RESPONSE_DATA);

  // Has Bluetooth Radio transport
  protected static final byte[] REGISTRATION_RESPONSE_DATA_ONE_TRANSPORT = parseHex(
      "0504b174bc49c7ca254b70d2e5c207cee9cf174820ebd77ea3c65508c26da51b657c1cc"
      + "6b952f8621697936482da0a6d3d3826a59095daf6cd7c03e2e60385d2f6d9402a552d"
      + "fdb7477ed65fd84133f86196010b2215b57da75d315b7b9e8fe2e3925a6019551bab6"
      + "1d16591659cbaf00b4950f7abfe6660e2e006f76868b772d70c253082019a30820140"
      + "a0030201020209012242000255962657300a06082a8648ce3d0403023045310b30090"
      + "603550406130241553113301106035504080c0a536f6d652d53746174653121301f06"
      + "0355040a0c18496e7465726e6574205769646769747320507479204c74643020170d3"
      + "135303830353136353131325a180f32303633303630373136353131325a3045310b30"
      + "090603550406130241553113301106035504080c0a536f6d652d53746174653121301"
      + "f060355040a0c18496e7465726e6574205769646769747320507479204c7464305930"
      + "1306072a8648ce3d020106082a8648ce3d030107034200042e09745f6e0f412a7a84b"
      + "367eb0b8dcb4a61d1fa336bbecfe30bd0a2c8faf74734a82fc03412589f4cc107f932"
      + "d3167e961eb664c3080347e505626c1d5d15cfa31730153013060b2b0601040182e51"
      + "c020101040403020780300a06082a8648ce3d040302034800304502202106e368bbe2"
      + "fc9f86991826b90a51c694b90fb7c01945e7a9531e4b65315ac5022100aa8e75a071e"
      + "645000376150c7faef1b8a57cb4bd41729c28d9b9bec744ebb4493045022070c1b332"
      + "667853491a525850b15599cc88be0433fc673be89e991b550921c2110221008326311"
      + "e0feaf1698110bed2c0737f3614298a8f265121f896db3cad459607fb");
  protected static final String REGISTRATION_RESPONSE_DATA_ONE_TRANSPORT_BASE64 =
      Base64.encodeBase64URLSafeString(REGISTRATION_RESPONSE_DATA_ONE_TRANSPORT);

  // Has Bluetooth Radio, Bluetooth Low Energy, and NFC transports
  protected static final byte[] REGISTRATION_RESPONSE_DATA_MULTIPLE_TRANSPORTS = parseHex(
      "0504b174bc49c7ca254b70d2e5c207cee9cf174820ebd77ea3c65508c26da51b657c1cc6"
      + "b952f8621697936482da0a6d3d3826a59095daf6cd7c03e2e60385d2f6d9402a552dfd"
      + "b7477ed65fd84133f86196010b2215b57da75d315b7b9e8fe2e3925a6019551bab61d1"
      + "6591659cbaf00b4950f7abfe6660e2e006f76868b772d70c253082019930820140a003"
      + "0201020209012242000255962657300a06082a8648ce3d0403023045310b3009060355"
      + "0406130241553113301106035504080c0a536f6d652d53746174653121301f06035504"
      + "0a0c18496e7465726e6574205769646769747320507479204c74643020170d31353038"
      + "30353136343932345a180f32303633303630373136343932345a3045310b3009060355"
      + "0406130241553113301106035504080c0a536f6d652d53746174653121301f06035504"
      + "0a0c18496e7465726e6574205769646769747320507479204c74643059301306072a86"
      + "48ce3d020106082a8648ce3d030107034200042e09745f6e0f412a7a84b367eb0b8dcb"
      + "4a61d1fa336bbecfe30bd0a2c8faf74734a82fc03412589f4cc107f932d3167e961eb6"
      + "64c3080347e505626c1d5d15cfa31730153013060b2b0601040182e51c020101040403"
      + "0204d0300a06082a8648ce3d0403020347003044022058b52f205dc9772e1bef915973"
      + "6098290ffb5850769efd1c37cfc97141279e5f02200c4d91c96c457d1a607a0d16b0b5"
      + "47bbb2e5e2865490112e4b94607b3adcad18304402202548b5204488995f00c905d2b9"
      + "25ca2f9b8c0aba76faf3461dc6778864eb5ee3022005f2d852969864577e01c71cbb10"
      + "93412ef0fef518141d698cda2a45fe2bc767");
  protected static final String REGISTRATION_RESPONSE_DATA_MULTIPLE_TRANSPORTS_BASE64 =
      Base64.encodeBase64URLSafeString(REGISTRATION_RESPONSE_DATA_MULTIPLE_TRANSPORTS);

  protected static final byte[] REGISTRATION_RESPONSE_DATA_MALFORMED_TRANSPORTS = parseHex(
      "0504b174bc49c7ca254b70d2e5c207cee9cf174820ebd77ea3c65508c26da51b657c1cc6b"
      + "952f8621697936482da0a6d3d3826a59095daf6cd7c03e2e60385d2f6d9402a552dfdb7"
      + "477ed65fd84133f86196010b2215b57da75d315b7b9e8fe2e3925a6019551bab61d1659"
      + "1659cbaf00b4950f7abfe6660e2e006f76868b772d70c25308201983082013ea0030201"
      + "020209012242000255962657300a06082a8648ce3d0403023045310b300906035504061"
      + "30241553113301106035504080c0a536f6d652d53746174653121301f060355040a0c18"
      + "496e7465726e6574205769646769747320507479204c74643020170d313530383036323"
      + "3333532385a180f32303633303630383233333532385a3045310b300906035504061302"
      + "41553113301106035504080c0a536f6d652d53746174653121301f060355040a0c18496"
      + "e7465726e6574205769646769747320507479204c74643059301306072a8648ce3d0201"
      + "06082a8648ce3d030107034200042e09745f6e0f412a7a84b367eb0b8dcb4a61d1fa336"
      + "bbecfe30bd0a2c8faf74734a82fc03412589f4cc107f932d3167e961eb664c3080347e5"
      + "05626c1d5d15cfa31530133011060b2b0601040182e51c0201010402aa80300a06082a8"
      + "648ce3d0403020348003045022100907f965f33d857982b39d9f4c22ccb4a63359fc10a"
      + "af08a81997c0e04b73dc9b02204f45d556ae2ea71a5fdfa646b516584dada84954a5d8b"
      + "9d27bdb041e89b216b6304402206b5085168e0c0e850677d3423c0f3972860bd3fbf6d2"
      + "d98cd7af9e1d3f46269402201bde430c86260666bcaa23155296bd0627a8e48d98c2009"
      + "212bec8a7a77f7974");
  protected static final String REGISTRATION_RESPONSE_DATA_MALFORMED_TRANSPORTS_BASE64 =
      Base64.encodeBase64URLSafeString(REGISTRATION_RESPONSE_DATA_MALFORMED_TRANSPORTS);

  protected static final byte[] KEY_HANDLE = parseHex(
      "2a552dfdb7477ed65fd84133f86196010b2215b57da75d315b7b9e8fe2e3925a"
          + "6019551bab61d16591659cbaf00b4950f7abfe6660e2e006f76868b772d70c25");
  protected static final String KEY_HANDLE_BASE64 = Base64.encodeBase64URLSafeString(KEY_HANDLE);
  protected static final byte[] USER_PUBLIC_KEY_ENROLL_HEX = parseHex(
      "04b174bc49c7ca254b70d2e5c207cee9cf174820ebd77ea3c65508c26da51b65"
          + "7c1cc6b952f8621697936482da0a6d3d3826a59095daf6cd7c03e2e60385d2f6"
          + "d9");
  protected static final String USER_PRIVATE_KEY_ENROLL_HEX =
      "9a9684b127c5e3a706d618c86401c7cf6fd827fd0bc18d24b0eb842e36d16df1";
  protected static final PublicKey USER_PUBLIC_KEY_ENROLL =
      parsePublicKey(USER_PUBLIC_KEY_ENROLL_HEX);
  protected static final PrivateKey USER_PRIVATE_KEY_ENROLL =
      parsePrivateKey(USER_PRIVATE_KEY_ENROLL_HEX);
  protected static final KeyPair USER_KEY_PAIR_ENROLL = new KeyPair(USER_PUBLIC_KEY_ENROLL,
      USER_PRIVATE_KEY_ENROLL);
  protected static final String USER_PRIVATE_KEY_SIGN_HEX =
      "ffa1e110dde5a2f8d93c4df71e2d4337b7bf5ddb60c75dc2b6b81433b54dd3c0";
  protected static final byte[] USER_PUBLIC_KEY_SIGN_HEX = parseHex(
      "04d368f1b665bade3c33a20f1e429c7750d5033660c019119d29aa4ba7abc04a"
          + "a7c80a46bbe11ca8cb5674d74f31f8a903f6bad105fb6ab74aefef4db8b0025e"
          + "1d");
  protected static final PublicKey USER_PUBLIC_KEY_SIGN =
      parsePublicKey(USER_PUBLIC_KEY_SIGN_HEX);
  protected static final PrivateKey USER_PRIVATE_KEY_SIGN =
      parsePrivateKey(USER_PRIVATE_KEY_SIGN_HEX);
  protected static final KeyPair USER_KEY_PAIR_SIGN = new KeyPair(USER_PUBLIC_KEY_SIGN,
      USER_PRIVATE_KEY_SIGN);
  protected static final byte[] SIGN_REQUEST_DATA = parseHex(
      "03ccd6ee2e47baef244d49a222db496bad0ef5b6f93aa7cc4d30c4821b3b9dbc"
          + "574b0be934baebb5d12d26011b69227fa5e86df94e7d94aa2949a89f2d493992"
          + "ca402a552dfdb7477ed65fd84133f86196010b2215b57da75d315b7b9e8fe2e3"
          + "925a6019551bab61d16591659cbaf00b4950f7abfe6660e2e006f76868b772d7"
          + "0c25");
  protected static final byte[] SIGN_RESPONSE_DATA = parseHex(
      "0100000001304402204b5f0cd17534cedd8c34ee09570ef542a353df4436030c"
          + "e43d406de870b847780220267bb998fac9b7266eb60e7cb0b5eabdfd5ba9614f"
          + "53c7b22272ec10047a923f");
  protected static final String SIGN_RESPONSE_DATA_BASE64 = Base64
      .encodeBase64URLSafeString(SIGN_RESPONSE_DATA);
  protected static final byte[] EXPECTED_REGISTER_SIGNED_BYTES = parseHex(
      "00f0e6a6a97042a4f1f1c87f5f7d44315b2d852c2df5c7991cc66241bf7072d1"
          + "c44142d21c00d94ffb9d504ada8f99b721f4b191ae4e37ca0140f696b6983cfa"
          + "cb2a552dfdb7477ed65fd84133f86196010b2215b57da75d315b7b9e8fe2e392"
          + "5a6019551bab61d16591659cbaf00b4950f7abfe6660e2e006f76868b772d70c"
          + "2504b174bc49c7ca254b70d2e5c207cee9cf174820ebd77ea3c65508c26da51b"
          + "657c1cc6b952f8621697936482da0a6d3d3826a59095daf6cd7c03e2e60385d2"
          + "f6d9");
  protected static final byte[] EXPECTED_AUTHENTICATE_SIGNED_BYTES = parseHex(
      "4b0be934baebb5d12d26011b69227fa5e86df94e7d94aa2949a89f2d493992ca"
          + "0100000001ccd6ee2e47baef244d49a222db496bad0ef5b6f93aa7cc4d30c482"
          + "1b3b9dbc57");
  protected static final byte[] SIGNATURE_ENROLL = parseHex(
      "304502201471899bcc3987e62e8202c9b39c33c19033f7340352dba80fcab017"
          + "db9230e402210082677d673d891933ade6f617e5dbde2e247e70423fd5ad7804"
          + "a6d3d3961ef871");
  protected static final byte[] SIGNATURE_AUTHENTICATE = parseHex(
      "304402204b5f0cd17534cedd8c34ee09570ef542a353df4436030ce43d406de8"
          + "70b847780220267bb998fac9b7266eb60e7cb0b5eabdfd5ba9614f53c7b22272"
          + "ec10047a923f");

  // Test vectors provided by Discretix
  protected static final String APP_ID_2 = APP_ID_ENROLL;
  protected static final String CHALLENGE_2_BASE64 = SERVER_CHALLENGE_ENROLL_BASE64;
  protected static final String BROWSER_DATA_2_BASE64 = BROWSER_DATA_ENROLL_BASE64;

  protected static final String TRUSTED_CERTIFICATE_2_HEX =
      "308201443081eaa0030201020209019189ffffffff5183300a06082a8648ce3d"
          + "040302301b3119301706035504031310476e756262792048534d204341203030"
          + "3022180f32303132303630313030303030305a180f3230363230353331323335"
          + "3935395a30303119301706035504031310476f6f676c6520476e756262792076"
          + "3031133011060355042d030a00019189ffffffff51833059301306072a8648ce"
          + "3d020106082a8648ce3d030107034200041f1302f12173a9cbea83d06d755411"
          + "e582a87fbb5850eddcf3607ec759a4a12c3cb392235e8d5b17caee1b34e5b5eb"
          + "548649696257f0ea8efb90846f88ad5f72300a06082a8648ce3d040302034900"
          + "3046022100b4caea5dc60fbf9f004ed84fc4f18522981c1c303155c08274e889"
          + "f3f10c5b23022100faafb4f10b92f4754e3b08b5af353f78485bc903ece7ea91"
          + "1264fc1673b6598f";
  protected static final X509Certificate TRUSTED_CERTIFICATE_2 =
      parseCertificate(TRUSTED_CERTIFICATE_2_HEX);

  // Has Bluetooth Radio transport
  private static final String TRUSTED_CERTIFICATE_ONE_TRANSPORT_BASE64 =
      "MIIBmjCCAUCgAwIBAgIJASJCAAJVliZXMAoGCCqGSM49BAMCMEUxCzAJBgNVBAYT"
          + "AkFVMRMwEQYDVQQIDApTb21lLVN0YXRlMSEwHwYDVQQKDBhJbnRlcm5ldCBXaWRn"
          + "aXRzIFB0eSBMdGQwIBcNMTUwODA1MTY1MTEyWhgPMjA2MzA2MDcxNjUxMTJaMEUx"
          + "CzAJBgNVBAYTAkFVMRMwEQYDVQQIDApTb21lLVN0YXRlMSEwHwYDVQQKDBhJbnRl"
          + "cm5ldCBXaWRnaXRzIFB0eSBMdGQwWTATBgcqhkjOPQIBBggqhkjOPQMBBwNCAAQu"
          + "CXRfbg9BKnqEs2frC43LSmHR+jNrvs/jC9CiyPr3RzSoL8A0ElifTMEH+TLTFn6W"
          + "HrZkwwgDR+UFYmwdXRXPoxcwFTATBgsrBgEEAYLlHAIBAQQEAwIHgDAKBggqhkjO"
          + "PQQDAgNIADBFAiAhBuNou+L8n4aZGCa5ClHGlLkPt8AZReepUx5LZTFaxQIhAKqO"
          + "daBx5kUAA3YVDH+u8bilfLS9QXKcKNm5vsdE67RJ";
  protected static final X509Certificate TRUSTED_CERTIFICATE_ONE_TRANSPORT =
      parseCertificateBase64(TRUSTED_CERTIFICATE_ONE_TRANSPORT_BASE64);

  // Has Bluetooth Radio, Bluetooth Low Energy, and NFC transports
  private static final String TRUSTED_CERTIFICATE_MULTIPLE_TRANSPORTS_BASE64 =
      "MIIBmTCCAUCgAwIBAgIJASJCAAJVliZXMAoGCCqGSM49BAMCMEUxCzAJBgNVBAYT"
          + "AkFVMRMwEQYDVQQIDApTb21lLVN0YXRlMSEwHwYDVQQKDBhJbnRlcm5ldCBXaWRn"
          + "aXRzIFB0eSBMdGQwIBcNMTUwODA1MTY0OTI0WhgPMjA2MzA2MDcxNjQ5MjRaMEUx"
          + "CzAJBgNVBAYTAkFVMRMwEQYDVQQIDApTb21lLVN0YXRlMSEwHwYDVQQKDBhJbnRl"
          + "cm5ldCBXaWRnaXRzIFB0eSBMdGQwWTATBgcqhkjOPQIBBggqhkjOPQMBBwNCAAQu"
          + "CXRfbg9BKnqEs2frC43LSmHR+jNrvs/jC9CiyPr3RzSoL8A0ElifTMEH+TLTFn6W"
          + "HrZkwwgDR+UFYmwdXRXPoxcwFTATBgsrBgEEAYLlHAIBAQQEAwIE0DAKBggqhkjO"
          + "PQQDAgNHADBEAiBYtS8gXcl3LhvvkVlzYJgpD/tYUHae/Rw3z8lxQSeeXwIgDE2R"
          + "yWxFfRpgeg0WsLVHu7Ll4oZUkBEuS5RgezrcrRg=";
  protected static final X509Certificate TRUSTED_CERTIFICATE_MULTIPLE_TRANSPORTS =
      parseCertificateBase64(TRUSTED_CERTIFICATE_MULTIPLE_TRANSPORTS_BASE64);

  private static final String TRUSTED_CERTIFICATE_MALFORMED_TRANSPORTS_EXTENSION_BASE64 =
      "MIIBmDCCAT6gAwIBAgIJASJCAAJVliZXMAoGCCqGSM49BAMCMEUxCzAJBgNVBAYT"
          + "AkFVMRMwEQYDVQQIDApTb21lLVN0YXRlMSEwHwYDVQQKDBhJbnRlcm5ldCBXaWRn"
          + "aXRzIFB0eSBMdGQwIBcNMTUwODA2MjMzNTI4WhgPMjA2MzA2MDgyMzM1MjhaMEUx"
          + "CzAJBgNVBAYTAkFVMRMwEQYDVQQIDApTb21lLVN0YXRlMSEwHwYDVQQKDBhJbnRl"
          + "cm5ldCBXaWRnaXRzIFB0eSBMdGQwWTATBgcqhkjOPQIBBggqhkjOPQMBBwNCAAQu"
          + "CXRfbg9BKnqEs2frC43LSmHR+jNrvs/jC9CiyPr3RzSoL8A0ElifTMEH+TLTFn6W"
          + "HrZkwwgDR+UFYmwdXRXPoxUwEzARBgsrBgEEAYLlHAIBAQQCqoAwCgYIKoZIzj0E"
          + "AwIDSAAwRQIhAJB/ll8z2FeYKznZ9MIsy0pjNZ/BCq8IqBmXwOBLc9ybAiBPRdVW"
          + "ri6nGl/fpka1FlhNrahJVKXYudJ72wQeibIWtg==";
  protected static final X509Certificate TRUSTED_CERTIFICATE_MALFORMED_TRANSPORTS_EXTENSION =
      parseCertificateBase64(TRUSTED_CERTIFICATE_MALFORMED_TRANSPORTS_EXTENSION_BASE64);

  protected static final byte[] REGISTRATION_DATA_2 = parseHex(
      "0504478E16BBDBBB741A660A000314A8B6BD63095196ED704C52EEBC0FA02A61"
          + "8F19FF59DF18451A11CEE43DEFD9A29B5710F63DFC671F752B1B0C6CA76C8427"
          + "AF2D403C2415E1760D1108105720C6069A9039C99D09F76909C36D9EFC350937"
          + "31F85F55AC6D73EA69DE7D9005AE9507B95E149E19676272FC202D949A3AB151"
          + "B96870308201443081EAA0030201020209019189FFFFFFFF5183300A06082A86"
          + "48CE3D040302301B3119301706035504031310476E756262792048534D204341"
          + "2030303022180F32303132303630313030303030305A180F3230363230353331"
          + "3233353935395A30303119301706035504031310476F6F676C6520476E756262"
          + "7920763031133011060355042D030A00019189FFFFFFFF51833059301306072A"
          + "8648CE3D020106082A8648CE3D030107034200041F1302F12173A9CBEA83D06D"
          + "755411E582A87FBB5850EDDCF3607EC759A4A12C3CB392235E8D5B17CAEE1B34"
          + "E5B5EB548649696257F0EA8EFB90846F88AD5F72300A06082A8648CE3D040302"
          + "0349003046022100B4CAEA5DC60FBF9F004ED84FC4F18522981C1C303155C082"
          + "74E889F3F10C5B23022100FAAFB4F10B92F4754E3B08B5AF353F78485BC903EC"
          + "E7EA911264FC1673B6598F3046022100F3BE1BF12CBF0BE7EAB5EA32F3664EDB"
          + "18A24D4999AAC5AA40FF39CF6F34C9ED022100CE72631767367467DFE2AECF6A"
          + "5A4EBA9779FAC65F5CA8A2C325B174EE4769AC");
  protected static final String REGISTRATION_DATA_2_BASE64 = Base64
      .encodeBase64URLSafeString(REGISTRATION_DATA_2);
  protected static final byte[] KEY_HANDLE_2 = parseHex(
      "3c2415e1760d1108105720c6069a9039c99d09f76909c36d9efc35093731f85f"
          + "55ac6d73ea69de7d9005ae9507b95e149e19676272fc202d949a3ab151b96870");
  protected static final String KEY_HANDLE_2_BASE64 =
      Base64.encodeBase64URLSafeString(KEY_HANDLE_2);
  protected static final byte[] USER_PUBLIC_KEY_2 = parseHex(
      "04478e16bbdbbb741a660a000314a8b6bd63095196ed704c52eebc0fa02a618f"
          + "19ff59df18451a11cee43defd9a29b5710f63dfc671f752b1b0c6ca76c8427af"
          + "2d");
  protected static final byte[] SIGN_DATA_2 = parseHex(
      "01000000223045022100FB16D12F8EC73D93EAB43BFDF141BF94E31AD3B1C98E"
          + "E4459E9E80CBBBD892F70220796DBCB8BBF57EC95A20A76D9ED3365CB688BF88"
          + "2ECCEABCC8D4A674024F6ABA");
  protected static final String SIGN_DATA_2_BASE64 = Base64.encodeBase64URLSafeString(SIGN_DATA_2);
}
