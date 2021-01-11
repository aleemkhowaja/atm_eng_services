package com.path.atm.engine.iso8583;

/**
 * Constants purely related to iso and not to any engine configuration
 * 
 * @author MohammadAliMezzawi
 *
 */
public class IsoDefinitionConstants
{

    public final static char MASK_CHAR = '*';
    public final static char[] MASKED_VALUE = { '*', '*', '*', '*' };
    public final static char MASK_SHOW_BIT = '1';
    public final static char MASK_HIDE_BIT = '0';

    /* 34 PAN extended, 35 track 2, 36 track 3, 45 track 1 */
    public static final int[] DEFAULT_MASKED_FIELDS = { 34, 35, 36, 45 };

    // Iso HashMap constants
    public final static String ISO_HM_FIELD_PREFIX = "ISO";
    public final static String ISO_HM_SUBFIELD_SEPARATOR = ".";

    // Iso Message definition Key pattern
    public final static String ISO_MSG_DEF_KEY_MASK = "XXXX";
    public final static String ISO_MSG_DEF_KEY_JOINER = "_";

    /**
     * Length type
     */
    public final static String ISO_LENGTH_TYPE_256 = "1";
    public final static String ISO_LENGTH_TYPE_10 = "2";

    /**
     * ISO Custom Fields List of custom fields related to imal
     */
    public final static String ISO_FIELD_MTICODE = "MTICODE";
    public final static String ISO_FIELD_COMPCODE = "COMPCODE";
    public final static String ISO_FIELD_COMPNAME = "COMPNAME";
    public final static String ISO_FIELD_BRCODE = "BRCODE";
    public final static String ISO_FIELD_TELLCODE = "TELLCODE";
    public final static String ISO_FIELD_TELLERUSER = "TELLUSER";
    public final static String ISO_FIELD_TRXCODE = "TRXTYPE";
    public final static String ISO_FIELD_CHARGETRXCODE = "CHRGTRXTYPE";
    public final static String ISO_FIELD_MERACC = "MERACC";
    public final static String ISO_FIELD_TERMINAL = "TERMINALID";
    public final static String ISO_FIELD_TERMDESC = "TRMNLDESC";

    // @todo check if we need timeout
    public final static String ISO_FIELD_AUTHCODE = "AUTHCODE";
    public final static String ISO_FIELD_RESP_RESULT = "ISOErrorCode";
    public final static String ISO_FIELD_INTERFACE_CODE = "INTERFACECODE";
    public final static String ISO_FIELD_CORE_TRX_NB = "TRXNB";
    public final static String ISO_FIELD_CORE_ERROR_DESC = "COREERRORDESC";
    public final static String ISO_FIELD_PWS_REQ_ID = "PWSREQID";

    public final static String ISO_HEADER = "ISOHEAD";

    /**
     * Network message type
     */
    public final static String NET_MSG_LOGON = "LO";
    public final static String NET_MSG_LOGOFF = "LOF";
    public final static String NET_MSG_ECHO = "EC";

    // Response code type
    public static final String RESP_TYPE_CORE = "CORE";
    public static final String RESP_TYPE_TECHNICAL = "TECHNICAL";

    // ISO message definition
    public final static short AUTH_REQ = 1100; // authorization request
    public final static short AUTH_REQ_REPEAT = 1101; // authorization request
						      // repect
    public final static short AUTH_REQ_RESP = 1110; // authorization request
						    // response
    public final static short AUTH_ADVICE = 1120; // authorization advice
    public final static short AUTH_ADVICE_REPEAT = 1121; // authorization advice
							 // repect
    public final static short AUTH_ADVICE_RESP = 1130; // authorization afvice
						       // response
    public final static short AUTH_NOTIFY = 1140; // authorization notification

    // financial messages
    public final static short FIN_REQ = 1200; // financial request

    public final static short FIN_REQ_REPEAT = 1201; // financial request repect
    public final static short FIN_REQ_RESP = 1210; // financial request response
    public final static short FIN_ADVICE = 1220; // financial advice
    public final static short FIN_ADVICE_REPEAT = 1221; // financial advice
							// repect
    public final static short FIN_ADVICE_RESP = 1230; // financial advice
						      // response
    public final static short FIN_NOTIFY = 1240; // financial notification

    // file action messages
    public final static short FILE_REQ = 1304; // file action request
    public final static short FILE_REQ_REPEAT = 1305; // file action request
						      // repect
    public final static short FILE_REQ_RESP = 1314; // file action request
						    // response
    public final static short FILE_ADVICE = 1324; // file action advice
    public final static short FILE_ADVICE_REPEAT = 1325; // file action advice
							 // repect
    public final static short FILE_ADVICE_RESP = 1334; // file action advice
						       // response
    public final static short FILE_NOTIFY = 1344; // file action notification

    // reversal/chargeback messages
    public final static short REV_ADVICE = 1420; // reversal advice
    public final static short REV_ADVICE_REPEAT = 1421; // reversal advice
							// repect
    public final static short REV_ADVICE_RESP = 1430; // reversal advice
						      // response
    public final static short REV_ADVICE_NOTIFY = 1440; // reversal notification
    public final static short CHARGE_ADVICE = 1422; // chargeback advice
    public final static short CHARGE_ADVICE_REPEAT = 1423; // chargeback advice
							   // repect
    public final static short CHARGE_ADVICE_RESP = 1432; // chargeback advice
							 // response
    public final static short CHARGE_NOTIFY = 1442; // chargeback advice
						    // notification

    // reconciliation messages
    public final static short ACQ_RECON_REQ = 1500; // acquirer reconciliation
						    // request
    public final static short ACQ_RECON_REQ_REPEAT = 1501; // acquirer
							   // reconciliation
							   // request repect
    public final static short ACQ_RECON_REQ_RESP = 1510; // acquirer
							 // reconciliation
							 // request response
    public final static short ACQ_RECON_ADVICE = 1520; // acquirer
						       // reconciliation advice
    public final static short ACQ_RECON_ADVICE_REPEAT = 1521; // acquirer
							      // reconciliation
							      // advice repect
    public final static short ACQ_RECON_ADVICE_RESP = 1530; // acquirer
							    // reconciliation
							    // advice response
    public final static short ACQ_RECON_NOTIFY = 1540; // acquirer
						       // reconciliation
						       // notification
    public final static short ISSR_RECON_REQ = 1502; // issuer reconciliation
						     // request
    public final static short ISSR_RECON_REQ_REPEAT = 1503; // issuer
							    // reconciliation
							    // request repect
    public final static short ISSR_RECON_REQ_RESP = 1512; // issuer
							  // reconciliation
							  // request response
    public final static short ISSR_RECON_ADVICE = 1522; // issuer reconciliation
							// advice
    public final static short ISSR_RECON_ADVICE_REPEAT = 1523; // issuer
							       // reconciliation
							       // advice repect
    public final static short ISSR_RECON_ADVICE_RESP = 1532; // issuer
							     // reconciliation
							     // afvice response
    public final static short ISSR_RECON_NOTIFY = 1542; // issuer reconciliation
							// notification

    // administrative messages
    public final static short ADMIN_REQ = 1604; // administrative request
    public final static short ADMIN_REQ_REPEAT = 1605; // administrative request
						       // repect
    public final static short ADMIN_REQ_RESP = 1614; // administrative request
						     // response
    public final static short ADMIN_ADVICE = 1624; // administrative advice
    public final static short ADMIN_ADVICE_REPEAT = 1625; // administrative
							  // advice repect
    public final static short ADMIN_ADVICE_RESP = 1634; // administrative advice
							// response
    public final static short ADMIN_NOTIFY = 1644; // administrative
						   // notification

    // fee collection messages
    public final static short ACQ_FEE_ADVICE = 1720; // acquirer fee collection
						     // advice
    public final static short ACQ_FEE_ADVICE_REPEAT = 1721; // acquirer fee
							    // collection advice
							    // repect
    public final static short ACQ_FEE_ADVICE_RESP = 1730; // acquirer fee
							  // collection advice
							  // response
    public final static short ACQ_FEE_NOTIFY = 1740; // acquirer fee collection
						     // notification
    public final static short ISSR_FEE_ADVICE = 1722; // issuer fee collection
						      // advice
    public final static short ISSR_FEE_ADVICE_REPEAT = 1723; // issuer fee
							     // collection
							     // advice repect
    public final static short ISSR_FEE_ADVICE_RESP = 1732; // issuer fee
							   // collection advice
							   // response
    public final static short ISSR_FEE_NOTIFY = 1742; // issuer fee collection
						      // notification

    // network management messages
    public final static short NET_MGMT_REQ = 1804; // network management request
    public final static short NET_MGMT_REQ_REPEAT = 1805; // network management
							  // request repect
    public final static short NET_MGMT_REQ_RESP = 1814; // network management
							// request response
    public final static short NET_MGMT_ADVICE = 1824; // network management
						      // advice
    public final static short NET_NGMT_ADVICE_REPEAT = 1825; // network
							     // management
							     // advice repect
    public final static short NET_MGMT_ADVICE_RESP = 1834; // network management
							   // advice response
    public final static short NET_MGMT_NOTIFY = 1844; // network management
						      // notification

    /**
     * @todo Change to enumeration
     */
    // ISO fields definition
    public final static short BIT_SECONDARY_BITMAP = 1;
    public final static short BIT_PRIMARY_ACCOUNT_NUMBER = 2;
    public final static short BIT_PROCESSING_CODE = 3;
    public final static short BIT_TRANSACTION_AMOUNT = 4;
    public final static short BIT_RECONCILIATION_AMOUNT = 5;
    public final static short BIT_BILLING_AMOUNT = 6;
    public final static short BIT_TRANSMISSION_DATE_TIME = 7;
    public final static short BIT_BILLING_FEE_AMOUNT = 8;
    public final static short BIT_RECONCILIATION_CONVERSION_RATE = 9;
    public final static short BIT_BILLING_CONVERSION_RATE = 10;
    public final static short BIT_SYSTEM_TRACE = 11;
    public final static short BIT_LOCAL_DATE_TIME = 12;
    public final static short BIT_EFFECTIVE_DATE = 13;
    public final static short BIT_EXPIRY_DATE = 14;
    public final static short BIT_SETTLEMENT_DATE = 15;
    public final static short BIT_CONVERSION_DATE = 16;
    public final static short BIT_CAPTURE_DATE = 17;
    public final static short BIT_MERCHANT_TYPE = 18;
    public final static short BIT_ACQUIRER_COUNTRY_CODE = 19;
    public final static short BIT_PAN_COUNTRY_CODE = 20;
    public final static short BIT_FORWARD_COUNTRY_CODE = 21;
    public final static short BIT_POS_DATA_CODE = 22;
    public final static short BIT_CARD_SEQ_NUMBER = 23;
    public final static short BIT_FUNCTION_CODE = 24;
    public final static short BIT_MSG_REASON_CODE = 25;
    public final static short BIT_ACCEPTOR_BUS_CODE = 26;
    public final static short BIT_APPROVAL_CODE_LEN = 27;
    public final static short BIT_RECONCILIATION_DATE = 28;
    public final static short BIT_RECONCILIATION_INDICATOR = 29;
    public final static short BIT_ORIGINAL_AMOUNT = 30;
    public final static short BIT_ACQUIRER_REFERENCE_DATA = 31;
    public final static short BIT_ACQUIRING_ID = 32;
    public final static short BIT_FORWARDING_ID = 33;
    public final static short BIT_EXTEND_PAN = 34;
    public final static short BIT_TRACK2 = 35;
    public final static short BIT_TRACK3 = 36;
    public final static short BIT_RETRIEVAL_NUMBER = 37;
    public final static short BIT_APPROVAL_CODE = 38;
    public final static short BIT_ACTION_CODE = 39;
    public final static short BIT_SERVICE_CODE = 40;
    public final static short BIT_TERMINAL_ID = 41;
    public final static short BIT_MERCHANT_ID = 42;
    public final static short BIT_MERCHANT_NAME = 43;
    public final static short BIT_ADDITIONAL_RESPONSE_DATA = 44;
    public final static short BIT_TRACK1 = 45;
    public final static short BIT_AMOUNT_FEE = 46;

    public final static short BIT_ADDIDATA_NATIONAL = 47;
    public final static short BIT_ADDIDATA_PRIVATE = 48;
    public final static short BIT_TXN_CURRENCY_CODE = 49;
    public final static short BIT_RECON_CURRENCY_CODE = 50;
    public final static short BIT_BILLING_CURRENCY_CODE = 51;
    public final static short BIT_ENCRYPTED_PIN = 52;
    public final static short BIT_SECURITY_CONTROL_INFO = 53;
    public final static short BIT_ADDITIONAL_AMOUNT = 54;
    public final static short BIT_IC_CARD_DATA = 55;
    public final static short BIT_ORIGINAL_DATA = 56;
    public final static short BIT_LIFE_CYCLE_CODE = 57;
    public final static short BIT_AUTH_AGENT_CODE = 58;
    public final static short BIT_TRANSPORT_DATA = 59;
    public final static short BIT_FIELD60 = 60;
    public final static short BIT_FIELD61 = 61;
    public final static short BIT_FIELD62 = 62;
    public final static short BIT_FIELD63 = 63;
    public final static short BIT_MAC1 = 64;

    public final static short BIT_FIELD65 = 65;
    public final static short BIT_ORIGINAL_FEE_AMOUNT = 66;
    public final static short BIT_EXTEND_PAYMENT_DATE = 67;
    public final static short BIT_RECEIVING_COUNTRY_CODE = 68;
    public final static short BIT_SETTLEMENT_COUNTRY_CODE = 69;
    public final static short BIT_AUTH_AGENT_COUNTRY_CODE = 70;
    public final static short BIT_MESSAGE_NUMBER = 71;
    public final static short BIT_DATA_RECORD = 72;
    public final static short BIT_ACTION_DATE = 73;
    public final static short BIT_CREDIT_NUMBER = 74;
    public final static short BIT_CREDIT_REV_NUMBER = 75;
    public final static short BIT_DEBIT_NUMBER = 76;
    public final static short BIT_DEBIT_REV_NUMBER = 77;
    public final static short BIT_TRANSFER_NUMBER = 78;
    public final static short BIT_TRANSFER_REV_NUMBER = 79;
    public final static short BIT_INQUIEY_NUMBER = 80;
    public final static short BIT_AUTHORIZATION_NUMBER = 81;
    public final static short BIT_INQUIRY_REV_NUMBER = 82;
    public final static short BIT_PAYMENT_NUMBER = 83;
    public final static short BIT_PAYMENT_REV_NUMBER = 84;
    public final static short BIT_FEE_COLLECTION_NUMBER = 85;
    public final static short BIT_CREDIT_AMOUNT = 86;
    public final static short BIT_CREDIT_REV_AMOUNT = 87;
    public final static short BIT_DEBIT_AMOUNT = 88;
    public final static short BIT_DEBIT_REV_AMOUNT = 89;
    public final static short BIT_AUTHORIZATION_REV_NUMBER = 90;
    public final static short BIT_TXN_DEST_COUNTRY_CODE = 91;
    public final static short BIT_TXN_ORIG_COUNTRY_CODE = 92;
    public final static short BIT_TXN_DEST_ID = 93;
    public final static short BIT_TXN_ORIG_ID = 94;
    public final static short BIT_ISSUER_REFER_DATA = 95;
    public final static short BIT_KEY_MGMT_DATA = 96;
    public final static short BIT_NET_AMOUNT = 97;
    public final static short BIT_PAYEE = 98;
    public final static short BIT_SETTLEMENT_ID_CODE = 99;
    public final static short BIT_RECEIVING_ID_CODE = 100;
    public final static short BIT_FILE_NAME = 101;
    public final static short BIT_ACCNT_ID1 = 102;
    public final static short BIT_ACCNT_ID2 = 103;
    public final static short BIT_TXN_DESCRIPTION = 104;

    public final static short BIT_CREDIT_CHARGEBACK_AMOUNT = 105;
    public final static short BIT_DEBIT_CHARGEBACK_AMOUNT = 106;
    public final static short BIT_CREDIT_CHARGEBACK_NUMBER = 107;
    public final static short BIT_DEBIT_CHARGEBACK_NUMBER = 108;
    public final static short BIT_CREDIT_FEE_AMOUNT = 109;
    public final static short BIT_DEBIT_FEE_AMOUNT = 110;
    public final static short BIT_FIELD111 = 111;
    public final static short BIT_FIELD112 = 112;
    public final static short BIT_FIELD113 = 113;
    public final static short BIT_FIELD114 = 114;
    public final static short BIT_FIELD115 = 115;
    public final static short BIT_FIELD116 = 116;
    public final static short BIT_FIELD117 = 117;
    public final static short BIT_FIELD118 = 118;
    public final static short BIT_FIELD119 = 119;
    public final static short BIT_FIELD120 = 120;
    public final static short BIT_FIELD121 = 121;
    public final static short BIT_FIELD122 = 122;
    public final static short BIT_FIELD123 = 123;
    public final static short BIT_FIELD124 = 124;
    public final static short BIT_FIELD125 = 125;
    public final static short BIT_FIELD126 = 126;
    public final static short BIT_FIELD127 = 127;
    public final static short BIT_MAC2 = 128;

    public final static short BIT_PRIMARY_ACCOUNT_NUMBER_LENGTH = 130;// 128+original
								      // number
    public final static short BIT_ACQUIRER_REFERENCE_DATA_LENGTH = 159;
    public final static short BIT_ACQUIRING_ID_LENGTH = 160;
    public final static short BIT_FORWARDING_ID_LENGTH = 161;
    public final static short BIT_EXTEND_PAN_LENGTH = 162;
    public final static short BIT_TRACK2_LENGTH = 163;
    public final static short BIT_TRACK3_LENGTH = 164;
    public final static short BIT_MERCHANT_NAME_LENGTH = 171;
    public final static short BIT_ADDITIONAL_RESPONSE_DATA_LENGTH = 172;
    public final static short BIT_TRACK1_LENGTH = 173;
    public final static short BIT_AMOUNT_FEE_LENGTH = 174;
    public final static short BIT_ADDIDATA_NATIONAL_LENGTH = 175;
    public final static short BIT_ADDIDATA_PRIVATE_LENGTH = 176;
    public final static short BIT_SECURITY_CONTROL_INFO_LENGTH = 181;
    public final static short BIT_ADDITIONAL_AMOUNT_LENGTH = 182;
    public final static short BIT_IC_CARD_DATA_LENGTH = 183;
    public final static short BIT_ORIGINAL_DATA_LENGTH = 184;
    public final static short BIT_AUTH_AGENT_CODE_LENGTH = 186;
    public final static short BIT_TRANSPORT_DATA_LENGTH = 187;
    public final static short BIT_FIELD60_LENGTH = 188;
    public final static short BIT_FIELD61_LENGTH = 189;
    public final static short BIT_FIELD62_LENGTH = 190;
    public final static short BIT_FIELD63_LENGTH = 191;
    public final static short BIT_ORIGINAL_FEE_AMOUNT_LENGTH = 194;
    public final static short BIT_DATA_RECORD_LENGTH = 200;
    public final static short BIT_TXN_DEST_ID_LENGTH = 221;
    public final static short BIT_TXN_ORIG_ID_LENGTH = 222;
    public final static short BIT_ISSUER_REFER_DATA_LENGTH = 223;
    public final static short BIT_KEY_MGMT_DATA_LENGTH = 224;
    public final static short BIT_SETTLEMENT_ID_CODE_LENGTH = 227;
    public final static short BIT_RECEIVING_ID_CODE_LENGTH = 228;
    public final static short BIT_FILE_NAME_LENGTH = 229;
    public final static short BIT_ACCNT_ID1_LENGTH = 230;
    public final static short BIT_ACCNT_ID2_LENGTH = 231;

    public final static short BIT_TXN_DESCRIPTION_LENGTH = 232;
    public final static short BIT_CREDIT_FEE_AMOUNT_LENGTH = 237;
    public final static short BIT_DEBIT_FEE_AMOUNT_LENGTH = 238;
    public final static short BIT_FIELD111_LENGTH = 239;
    public final static short BIT_FIELD112_LENGTH = 240;
    public final static short BIT_FIELD113_LENGTH = 241;
    public final static short BIT_FIELD114_LENGTH = 242;
    public final static short BIT_FIELD115_LENGTH = 243;
    public final static short BIT_FIELD116_LENGTH = 244;
    public final static short BIT_FIELD117_LENGTH = 245;
    public final static short BIT_FIELD118_LENGTH = 246;
    public final static short BIT_FIELD119_LENGTH = 247;
    public final static short BIT_FIELD120_LENGTH = 248;
    public final static short BIT_FIELD121_LENGTH = 249;
    public final static short BIT_FIELD122_LENGTH = 250;
    public final static short BIT_FIELD123_LENGTH = 251;
    public final static short BIT_FIELD124_LENGTH = 252;
    public final static short BIT_FIELD125_LENGTH = 253;
    public final static short BIT_FIELD126_LENGTH = 254;
    public final static short BIT_FIELD127_LENGTH = 255;

    // ISO 8583(1993) action code - field 39
    // 000-099 Used in 1110,1120,1121,1140 and 1210,1220,1221 and 1240 messages
    // to indicate
    // that the transaction has been approved.
    public final static short RC_APPROVED = 000; // approved
    public final static short RC_APPROVED_WITH_ID = 001; // honour with
							 // identification
    public final static short RC_APPROVED_PARTIAL = 002; // approved for partial
							 // amount
    public final static short RC_APPROVED_VIP = 003; // approved(VIP)
    public final static short RC_APPROVED_TRACK3 = 004; // approved; update
							// track 3
    public final static short RC_APPROVED_ACCT_SPEC = 005; // approved, account
							   // type specified by
							   // card issuer
    public final static short RC_APPROVED_PARTIAL_SPEC = 006; // approved for
							      // partial amount;
							      // account type
							      // specified by
							      // card issuer
    public final static short RC_APPROVED_ICC = 007; // approved, update ICC
    public final static short RC_APPROVED_NEED_CNFM = 80; // approved, but need
							  // confirmation(used
							  // for CIBC and NOVA
							  // debit card
							  // processing mode

    // 100-199 Used in 1110,1120,1121,1140 and 1210,1220,1221 and 1240 messages
    // to indicate
    // that the transaction has been processed for authorization by or on behalf
    // of
    // the card issuer and has been denied(not requiring a card pick-up)
    public final static short RC_DECLINED_DO_NOT_HONOUR = 100; // do not honour
    public final static short RC_DECLINED_EXPIRED_CARD = 101; // expired card
    public final static short RC_DECLINED_SUSPECTED = 102; // suspected fraud
    public final static short RC_DECLINED_CONTACT_ACQ = 103; // card acceptor
							     // contact acquirer
    public final static short RC_DECLINED_RESTRICTED = 104; // restricted card
    public final static short RC_DECLINED_CALL_ACQ = 105; // card acceptor call
							  // acquirer's security
							  // department
    public final static short RC_DECLINED_PIN_EXCEED = 106; // allowable PIN
							    // tries exceeded
    public final static short RC_DECLINED_REFER_ISSR = 107; // refer to card
							    // issuer
    public final static short RC_DECLINED_REFER_ISSR_COND = 108; // refer to
								 // card
								 // issuer's
								 // special
								 // conditions
    public final static short RC_DECLINED_INVALID_MERCHANT = 109; // invalid
								  // merchant
    public final static short RC_DECLINED_INVALID_AMOUNT = 110; // invalid
								// amount
    public final static short RC_DECLINED_INVALID_CARD = 111; // invalid card
							      // number
    public final static short RC_DECLINED_PIN_REQUIRED = 112; // PIN data
							      // required
    public final static short RC_DECLINED_UNACCEPT_FEE = 113; // unacceptable
							      // fee
    public final static short RC_DECLINED_ACCT_REQ = 114; // no account of type
							  // requested
    public final static short RC_DECLINED_FUNC_NOT_SUPPORT = 115; // requested
								  // function
								  // not
								  // supported
    public final static short RC_DECLINED_NOT_SUFF_FUNDS = 116; // not
								// sufficient
								// funds
    public final static short RC_DECLINED_INCORRECT_PIN = 117; // incorrect PIN
    public final static short RC_DECLINED_NO_CARD_RECORD = 118; // no card
								// record
    public final static short RC_DECLINED_NOT_ALLOW_CARDHOLDER = 119; // transaction
								      // not
								      // permitted
								      // to
								      // cardholder

    public final static short RC_DECLINED_NOT_ALLOW_TERMINAL = 120; // transaction
								    // not
								    // permitted
								    // to
								    // terminal
    public final static short RC_DECLINED_EXCEED_AMOUNT_LIMIT = 121; // exceeds
								     // withdrawal
								     // amount
								     // limit
    public final static short RC_DECLINED_VIOLATION = 122; // security violation
    public final static short RC_DECLINED_EXCEED_FREQ_LIMIT = 123; // exceeds
								   // withdrawal
								   // frequency
								   // limit
    public final static short RC_DECLINED_VIOLATION_LAW = 124; // violation of
							       // law
    public final static short RC_DECLINED_NOT_EFFECTIVE = 125; // card not
							       // effective
    public final static short RC_DECLINED_INVALID_PIN = 126; // invalid PIN
							     // block
    public final static short RC_DECLINED_PIN_LENGTH = 127; // PIN length error
    public final static short RC_DECLINED_PIN_SYNC = 128; // PIN key synch error
    public final static short RC_DECLINED_SUSPECTED_COUNTER = 129; // suspected
								   // counterfeit
								   // card

    // 200-299 Used in 1110,1120,1121,1140 and 1210,1220,1221 and 1240 messages
    // to indicate
    // that the transaction has been processed for authorization by or on behalf
    // of
    // the card issuer and has been denied requiring a card to be pick-up.
    public final static short RC_PICKUP_DO_NOT_HONOUR = 200; // do not honour
    public final static short RC_PICKUP_EXPIRED_CARD = 201; // expired card
    public final static short RC_PICKUP_SUSPECTED = 202; // suspected fraud
    public final static short RC_PICKUP_CONTACT_ACQ = 203; // card acceptor
							   // contact acquirer
    public final static short RC_PICKUP_RESTRICTED = 204; // restricted card
    public final static short RC_PICKUP_CALL_ACQ = 205; // card acceptor call
							// acquirer's security
							// department
    public final static short RC_PICKUP_PIN_EXCEED = 206; // allowable PIN tries
							  // exceeded
    public final static short RC_PICKUP_SPEC_COND = 207; // special condition
    public final static short RC_PICKUP_LOST = 208; // lost card
    public final static short RC_PICKUP_STOLEN = 209; // stolen card
    public final static short RC_PICKUP_SUSPECTED_COUNTER = 210; // suspected
								 // countfeid
								 // card

    // 300-399 Used in 1314 1324,1325 and 1344 messages to indicate the result
    // of the file action
    public final static short RC_FILE_SUCCESS = 300; // successful
    public final static short RC_FILE_NOT_SUPPORT = 301; // not supported by
							 // receiver
    public final static short RC_FILE_UNABLE_LOCATE_RECORD = 302; // unable to
								  // locate
								  // record on
								  // file
    public final static short RC_FILE_DUP_REPLACE = 303; // duplicate record;
							 // old record replaced
    public final static short RC_FILE_EDIT_ERROR = 304; // field edit error
    public final static short RC_FILE_LOCKED_OUT = 305; // file locked out
    public final static short RC_FILE_NOT_SUCCESS = 306; // not successful
    public final static short RC_FILE_FORMAT_ERROR = 307; // format error
    public final static short RC_FILE_DUP_REJECT = 308; // duplicate; new record
							// rejected
    public final static short RC_FILE_UNKNOWN = 309; // unknown file

    // 400-499 Used in 1430,1432,1440 and 1442 messages to indicate the result
    // of the
    // reversal or chargeback.
    public final static short RC_REVERSAL_ACCEPT = 400; // accepted

    // 500-599 Used in 1510,1512,1530 and 1532 messages to indicate the result
    // of a reconciliation.
    public final static short RC_RECON_IN_BALANCE = 500; // reconciled; in
							 // balance
    public final static short RC_RECON_OUT_BALANCE = 501; // reconciled; out
							  // balance
    public final static short RC_RECON_AMOUNT_NOT_RECON = 502; // amount not
							       // reconciled;
							       // total provided
    public final static short RC_RECON_TOTAL_NOT_AVAILABLE = 503; // totals not
								  // avalable
    public final static short RC_RECON_NOT_RECON = 504; // not reconciled;
							// totals provided

    // 600-699 Used in 1614;1624;1625 and 1644 messages.
    public final static short RC_ADMIN_ACCEPT = 600; // accepted
    public final static short RC_ADMIN_NOT_TRACE_ORIGIN = 601; // not able to
							       // trace back
							       // original
							       // transaction
    public final static short RC_ADMIN_INVALID_REFERENCE = 602; // invalid
								// reference
								// number
    public final static short RC_ADMIN_PAN_INCOMPATIBLE = 603; // reference
							       // number/PAN
							       // incompatible
    public final static short RC_ADMIN_PHOTO_NOT_AVAILABLE = 604; // POS
								  // photograph
								  // is not
								  // available
    public final static short RC_ADMIN_ITEM_SUPP = 605; // item supplied
    public final static short RC_ADMIN_DOC_NOT_SUPP = 606; // request cannot be
							   // fulfilled-required/requested
							   // documentation is
							   // not available

    // 700-799 Used in 1720;1721;1740;1722;1723 and 1742 messages.
    public final static short RC_FEE_ACCEPT = 700; // accepted

    // 800-901 Used in 1814;1824;1825 and 1844 messages.
    public final static short RC_NETWORK_ACCEPT = 800; // accepted
    public final static short RC_NETWORK_NO_LIABILITY = 900; // advice
							     // acknowledged; no
							     // financial
							     // liability
							     // accepted
    public final static short RC_NETWORK_LIABILITY = 901; // advice
							  // acknowledged;
							  // financial liability
							  // accepted

    // 902-949 Used in request response and advice response messages to indicate
    // transaction
    // could not be processed.
    public final static short RC_REJECT_INVALID_TXN = 902; // invalid
							   // transaction
    public final static short RC_REJECT_RE_ENTER_TXN = 903; // re-enter
							    // transaction
    public final static short RC_REJECT_FORMAT_ERROR = 904; // format error
    public final static short RC_REJECT_ACQ_NOT_SUPP = 905; // acquirer not
							    // supported by
							    // switch
    public final static short RC_REJECT_CUTOVER_IN_PROCESS = 906; // cutover in
								  // process
    public final static short RC_REJECT_ISSUER_INOPERATIVE = 907; // card issuer
								  // or switch
								  // inoperative
    public final static short RC_REJECT_DEST_NOT_FOUND = 908; // transaction
							      // destination
							      // cannot be found
							      // for routing
    public final static short RC_REJECT_SYSTEM_MALFUNCTION = 909; // system
								  // malfunction
    public final static short RC_REJECT_ISSUER_SIGNOFF = 910; // card issuer
							      // signed off
    public final static short RC_REJECT_ISSUER_TIMEOUT = 911; // card issuer
							      // timed out
    public final static short RC_REJECT_ISSUER_NOT_AVAILABLE = 912; // card
								    // issuer
								    // unavailable
    public final static short RC_REJECT_DUP_TRANSMISSION = 913; // duplicate
								// transmission
    public final static short RC_REJECT_NOT_TRACE_ORIGIN = 914; // not able to
								// trace back to
								// original
								// transaction
    public final static short RC_REJECT_CHECKPOINT_ERROR = 915; // reconciliation
								// cutover or
								// checkpoint
								// error
    public final static short RC_REJECT_MAC_ERROR = 916; // MAC incorrect
    public final static short RC_REJECT_MAC_KEY_SYNC = 917; // MAC key sync
							    // error
    public final static short RC_REJECT_NO_COMM_KEY = 918; // no communication
							   // keys available for
							   // use
    public final static short RC_REJECT_ENCRYPTION_KEY_SYNC = 919; // encryption
								   // key sync
								   // error
    public final static short RC_REJECT_SECURITY_ERROR_TRY_AGAIN = 920; // security
									// software/hardware
									// error
									// - try
									// again
    public final static short RC_REJECT_SECURITY_ERROR_NO_ACTION = 921; // security
									// software/hardware
									// error
									// - no
									// action
    public final static short RC_REJECT_MSGNO_ERROR = 922; // message number out
							   // of sequence
    public final static short RC_REJECT_REQ_IN_PROCESS = 923; // request in
							      // progress

    // 950-999 Used in advice response(1x3x) to indicate the reason for
    // rejection of the transfer
    // of financial liability.
    public final static short RC_REJECT_VIOLATION = 950; // violation of
							 // business arrangement

    public final static String getISOResponseCodeDescription(short isoResponseCode)
    {
	switch (isoResponseCode)
	{
	    case IsoDefinitionConstants.RC_REJECT_SYSTEM_MALFUNCTION:
		return "System malfunction";
	    case IsoDefinitionConstants.RC_DECLINED_FUNC_NOT_SUPPORT:
		return "Function not supported";
	    case IsoDefinitionConstants.RC_REJECT_ISSUER_NOT_AVAILABLE:
		return "Issuer not available";
	    case IsoDefinitionConstants.RC_REJECT_INVALID_TXN:
		return "Invalid transaction";
	    default:
		return "Code=" + isoResponseCode;
	}
    }

    // ISO 8583(1993) function code - field 24
    // 000-099 reserved for ISO use
    // 100-199 Used in 1100;1101;1120;1121 and 1140 messages
    public final static short FUNC_AUTH_ORIGAUTH_ACCUAMT = 100; // original
								// authorization
								// - amount
								// accurate
    public final static short FUNC_AUTH_ORIGAUTH_ESTIAMT = 101; // original
								// authorization
								// - amount
								// estimated
    public final static short FUNC_AUTH_REPLAUTH_ACCUAMT = 102; // replacement
								// authorization
								// - amount
								// accurate
    public final static short FUNC_AUTH_REPLAUTH_ESTIAMT = 103; // replacement
								// authorization
								// - amount
								// estimated
    public final static short FUNC_AUTH_RESUBM_ACCUAMT = 104; // resubmission -
							      // amount accurate
    public final static short FUNC_AUTH_RESUBM_ESTIAMT = 105; // resubmission -
							      // amount
							      // estimated
    public final static short FUNC_AUTH_SUPMAUTH_ACCUAMT = 106; // supplementary
								// authorization
								// - amount
								// accurate
    public final static short FUNC_AUTH_SUPMAUTH_ESTIAMT = 107; // supplementary
								// authorization
								// - amount
								// estimated
    public final static short FUNC_AUTH_INQUIRY = 108; // inquiry

    // 200-299 Used in 1200;1201,1220,1221 and 1240 message
    public final static short FUNC_FIN_ORIGFIN_TXN = 200; // original financial
							  // request/advice
    public final static short FUNC_FIN_PREVAUTH_SAMEAMT = 201; // previously
							       // approved
							       // authorization
							       // - amount same
    public final static short FUNC_FIN_PREVAUTH_DIFFAMT = 202; // previously
							       // approved
							       // authorization
							       // - amount
							       // differs
    public final static short FUNC_FIN_RESUB_PREVFIN_DENY = 203; // resubmission
								 // of a
								 // previously
								 // denied
								 // financial
								 // request
    public final static short FUNC_FIN_RESUB_PREVFIN_REVER = 204; // resubmission
								  // of a
								  // previously
								  // reversed
								  // financial
								  // transaction
    public final static short FUNC_FIN_FIRST_REPRE = 205; // first representment
    public final static short FUNC_FIN_SECOND_REPRE = 206; // second
							   // representment
    public final static short FUNC_FIN_THIRD_REPRE = 207; // third or subsequent
							  // representment

    // 300-399 Used in 1304,1305,1324,1325 and 1344 messages
    public final static short FUNC_FILE_UNASSI = 300; // unassigned
    public final static short FUNC_FILE_ADD_RECORD = 301; // addd record
    public final static short FUNC_FILE_CHG_RECORD = 302; // change record
    public final static short FUNC_FLLE_DEL_RECORD = 303; // delete record
    public final static short FUNC_FILE_REP_RECORD = 304; // replace record
    public final static short FUNC_FILE_INQ = 305; // inquiry
    public final static short FUNC_FILE_REP_FILE = 306; // replace file
    public final static short FUNC_FILE_ADD_FILE = 307; // add file
    public final static short FUNC_FILE_DEL_FILE = 308; // delete file
    public final static short FUNC_FILE_CARD_ADMIN = 309; // card administration

    // 400-449 Used in 1420,1421 and 1440 messages to indicate the function of
    // the reversal
    public final static short FUNC_REV_FULL = 400; // full reversal; transaction
						   // did not complete as
						   // approved
    public final static short FUNC_REV_PART = 401; // partial reversal;
						   // transaction did not
						   // complete for full amount

    // 450-499 Used in 1422,1423 and 1442 messages to indicate the function of
    // the chargeback
    public final static short FUNC_CHARG_FIRST_FULL = 450; // first chargeback;
							   // full
    public final static short FUNC_CHARG_SECOND_FULL = 451; // second
							    // chargeback; full
    public final static short FUNC_CHARG_THIRD_FULL = 452; // third or
							   // subsequent
							   // chargeback; full
    public final static short FUNC_CHARG_FIRST_PART = 453; // first chargeback;
							   // partial
    public final static short FUNC_CHARGE_SECOND_PART = 454; // second
							     // chargeback;
							     // partial
    public final static short FUNC_CHARGE_THIRD_PART = 455; // third or
							    // subsequent;
							    // partial

    // 500-599 Used in 1500,1501,1502,1503,1520,1521,1522,1523,1540 and 1542
    // messages
    public final static short FUNC_RECON_FINAL = 500; // final reconciliation
    public final static short FUNC_RECON_CHK_POINT = 501; // checkpoint
							  // reconciliation
    public final static short FUNC_RECON_FINAL_CURR = 502; // final
							   // reconciliation in
							   // a specified
							   // currency
    public final static short FUNC_RECON_CHK_POINT_CURR = 503; // checkpoint
							       // reconciliation
							       // in a specified
							       // currency
    public final static short FUNC_RECON_REQ = 504; // request for
						    // reconciliation totals
    public final static short FUNC_RECON_SETTLE = 570; // request to settle
// 600-649 Used in 1604,1605,1624,1625 and 1644 messages for retrievals.
    public final static short FUNC_ADMIN_ORIGRECP_RETRREQ = 600; // original
								 // receipt,
								 // retrieval
								 // request
    public final static short FUNC_ADMIN_ORIGRECP_RETRREQ_REPEAT = 601; // original
									// receipt,
									// repeat
									// retrieval
									// request
    public final static short FUNC_ADMIN_ORIGRECP_FULFILL = 602; // original
								 // receipt,
								 // fulfillment
    public final static short FUNC_ADMIN_COPY_RETRREQ = 603; // copy, retrieval
							     // request
    public final static short FUNC_ADMIN_COPY_RETRREQ_REPEAT = 604; // copy,
								    // repeat
								    // retrieval
								    // request
    public final static short FUNC_ADMIN_COPY_FULFILL = 605; // copyn;
							     // fulfillment
    public final static short FUNC_ADMIN_VEHICLE = 606; // vehicle rental
							// agreement
    public final static short FUNC_ADMIN_HOTEL = 607; // hotel charge detail
    public final static short FUNC_ADMIN_POS = 608; // POS photograph
    public final static short FUNC_ADMIN_DEVY = 609; // proof of delivery
    public final static short FUNC_ADMIN_IMPRINT = 610; // imprint

    // 650-699 Used in 1604,1605,1624,1625 and 1644 messages for administrative
    // messages.
    public final static short FUNC_ADMIN_NOT_PARSE = 650; // unable to parse
							  // message

    // 700-799 Used in 1720,1721,1740,1722,1723 and 1742 messages.
    public final static short FUNC_FEE_COLL = 700; // fee collection message
    public final static short FUNC_FEE_COLL_CANCEL = 701; // fee collection
							  // cancellation,
							  // full/partial

    // 800-899 Used in 1804,1805,1824,1825 and 1844 messages.
    public final static short FUNC_NETWORK_SIGNON = 801; // system
							 // condition/sign-on
    public final static short FUNC_NETWORK_SIGNOFF = 802; // system
							  // condition/sign-off
    public final static short FUNC_NETWORK_UNAVAIL = 803; // system
							  // condition/target
							  // system unavailable
    public final static short FUNC_NETWORK_BACKUP = 804; // system
							 // condition/message
							 // originator's system
							 // in backup
    public final static short FUNC_NETWORK_SPECIAL = 805; // system
							  // condition/special
							  // instruction
    public final static short FUNC_NETWORK_ROUTE = 806; // system
							// condition/initate
							// alternate routing

    public final static short FUNC_NETWORK_KEYCHANGE = 811; // system
							    // security/key
							    // change
    public final static short FUNC_NETWORK_ALERT = 812; // system
							// security/security
							// alert
    public final static short FUNC_NETWORK_PASSWDCHANGE = 813; // system
							       // security/password
							       // change
    public final static short FUNC_NETWORK_DEVICE_AUTH = 814; // system
							      // security/device
							      // authentication

    public final static short FUNC_NETWORK_CUTOVER = 821; // system
							  // accounting/cutover
    public final static short FUNC_NETWORK_CHK_POINT = 822; // system
							    // accounting/checkpoint

    public final static short FUNC_NETWORK_ECHO = 831; // system audit
						       // control/echo test

    // ISO 8583(1993) amount type codes - field ... ...
    // 00-19 account related balances
    // 00 reserved for ISO use
    public final static short AMT_LEDGER_BALANCE = 01; // account ledger balance
    public final static short AMT_AVAIL_BALANCE = 02; // account available
						      // balance
    public final static short AMT_OWING = 03; // account owning
    public final static short AMT_DUE = 04; // account due
    public final static short AMT_AVAIL_CREDIT = 05; // account available credit
    // 20-39 card related amounts
    public final static short AMT_REMAINING = 20; // amount remaining this cycle
    // 40-59 transaction related amounts
    public final static short AMT_CASH = 40; // amount cash
    public final static short AMT_GOODS = 41; // amount goods and services

    // ISO 8583(1993) processing code - field 3(12)
    // 00-19 debits
    public final static short TPC_DB_GOODS_AND_SERVICE = 00; // goods and
							     // service
    public final static short TPC_DB_CASH = 01; // cash
    public final static short TPC_DB_ADJUSTMENT = 02; // adjustment
    public final static short TPC_DB_CHEQUE_GUAR = 03; // cheque guarantee(funds
						       // guaranteed)
    public final static short TPC_DB_CHEQUE_VERI = 04; // cheque
						       // verification(funds
						       // available but not
						       // quaranteed)
    public final static short TPC_DB_EURO_CHEQUE = 05; // eurocheque
    public final static short TPC_DB_TRAVEL_CHEQUE = 06; // traveller cheque
    public final static short TPC_DB_LETTER_CREDIT = 07; // letter of credit
    public final static short TPC_DB_GIRO = 8; // giro(postal banking)
    public final static short TPC_DB_DISBURSE = 9; // goods and services with
						   // cash disbursement
    public final static short TPC_DB_NON_CASH = 10; // non-cash financial
						    // instrument(e.g.wire
						    // transfer)
    public final static short TPC_DB_QUASI = 11; // quasi-cash and scrip
    public final static short TPC_DB_SECOND_REQUEST = 17; // Tender Retail
							  // second request
							  // message
    // 20-29 credits
    public final static short TPC_CR_RETURE = 20; // returns
    public final static short TPC_CR_DEPOSIT = 21; // deposits
    public final static short TPC_CR_ADJUSTMENT = 22; // adjustment
    public final static short TPC_CR_CHEQUE_GUAR = 23; // cheque deposit
						       // guarantee
    public final static short TPC_CR_CHEQUE = 24; // cheque deposit
    public final static short TPC_CASH_ADJUSTMENT = 28; // cash adjustment
    // 30-39 inquiry services
    public final static short TPC_FUND_INQUIRY = 30; // available funds inquiry
    public final static short TPC_BALANCE_INQUIRY = 31; // balance inquiry
    // 40-49 transfer services
    public final static short TPC_TRANSFER = 40; // cardholder accounts transfer
    public final static short TPC_TRANSFER_ADJUSTMENT = 48; // void cardholder
							    // accounts transfer
    // 50-59 payment services
    public final static short TPC_PAYMENT = 50; // payment
    public final static short TPC_PAYMENT_ADJUSTMENT = 58; // payment adjustment
    // 90-99 reserved for private use
    public final static short TPC_ACTIVATE = 90; // account activation
    public final static short TPC_ACTIVATE_ADJUSTMENT = 91; // void account
							    // activation
    public final static short TPC_DEACTIVATE = 92; // account deactivation
    public final static short TPC_DEACTIVATE_ADJUSTMENT = 93; // void account
							      // deactivation
    // ISO 8583 (1993) account type - field 3(34/56)

    public final static short ACCNT_DEFAULT = 00; // default - unspecified
    public final static short ACCNT_SAVING = 10; // saving account - default
    public final static short ACCNT_CHEQUE = 20; // cheque account - default
    public final static short ACCNT_CREDIT = 30; // credit facility - default
    public final static short ACCNT_UNIVERSAL = 40; // universal account -
						    // default
    public final static short ACCNT_INVESTMENT = 50; // investment account -
						     // default
    public final static short ACCNT_PRIVATELABEL = 90; // private label card
    public final static short ACCNT_STOREVALUE = 91; // store value card
    public final static short ACCNT_GIFT = 92; // gift card

    // ISO Card Data Input Capability
    public final static short CARD_CAPTURE_UNKNOWN = (short) '0'; //
    public final static short CARD_CAPTURE_MANUAL = (short) '1';
    public final static short CARD_CAPTURE_SWIPED = (short) '2';
    public final static short CARD_CAPTURE_SCANNED = (short) '3';
    public final static short CARD_CAPTURE_OCR = (short) '4';
    public final static short CARD_CAPTURE_ICC = (short) '5';
    public final static short CARD_CAPTURE_KEYED = (short) '6';
    public final static short CARD_CAPTURE_RESERVED = (short) '7';

}