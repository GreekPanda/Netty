package com.innjoo.halo.proto;

public class ProtoOpType {
	
	public static final short HALO_PACKAGE_HEADRE_LEN = 14;

	public static final short HALO_CMD_REQ_LINK_FIRST = 0x0001;
	
	public static final short HALO_CMD_REQ_LINK_WITHDATA = (0x0002);
	public static final short HALO_CMD_REQ_LINK_WITHDATA_LEN = 118;

	public static final short SERVER_CMD_REQ_HISTORYDETAIL_SYNC = (0x0003);
	public static final short SERVER_CMD_REQ_HISTORYDETAIL_SYNC_LEN = 46;

	public static final short HALO_CMD_ACK_HISTORYDETAIL_SYNC = (0x0004);

	public static final short SERVER_CMD_ACK_HISTORYDETAIL_ACCEPT = (0x0005);
	public static final short SERVER_CMD_ACK_HISTORYDETAIL_ACCEPT_LEN = 4;

	public static final short HALO_CMD_ACK_HISTORYDETAIL_END = (0x0006);
	public static final short HALO_CMD_ACK_HISTORYDETAIL_END_LEN = 0;

	/* wifi »Ìº˛…˝º∂∂®“Â */
	public static final short HALO_CMD_ACK_VERSIONUPDATE = (0x0007);
	public static final short HALO_CMD_ACK_VERSIONUPDATE_LEN = 0;

	public static final short SERVER_CMD_REQ_VERSIONUPDATE = (0x0008);
	public static final short SERVER_CMD_REQ_VERSIONUPDATE_LEN = 14;

	public static final short HALO_CMD_ACK_VERSIONUPDATE_ACK = (0x0009);
	public static final short HALO_CMD_ACK_VERSIONUPDATE_ACK_LEN = 6;

	public static final short SERVER_CMD_REQ_VERSIONUPDATE_DATA = (0x000a);

	public static final short HALO_CMD_ACK_VERSIONUPDATE_DATA_ACCEPT = (0x000b);
	public static final short HALO_CMD_ACK_VERSIONUPDATE_DATA_ACCEPT_LEN = 8;

	public static final short SERVER_CMD_ACK_VERSIONUPDATE_END = (0x000c);
	public static final short SERVER_CMD_ACK_VERSIONUPDATE_END_LEN = 2;

	/* wifi»ÀŒÔΩ«…´∏¸–¬ */
	public static final short HALO_CMD_REQ_ROLEUPDATE = (0x000d);
	public static final short HALO_CMD_REQ_ROLEUPDATE_LEN = 2;
	
	public static final short HALO_CMD_REQ_ROLEUPDATE_RESP = (0x000E);
	public static final short HALO_CMD_REQ_ROLEUPDATE_RESP_LEN = 6;

	public static final short SERVER_CMD_REQ_ROLEUPDATE = (0x000f);
	public static final short SERVER_CMD_REQ_ROLEUPDATE_LEN = 6;

	public static final short HALO_CMD_ACK_ROLEUPDATE = (0x0010);
	public static final short HALO_CMD_ACK_ROLEUPDATE_LEN = 2;

	public static final short SERVER_CMD_REQ_ROLEUPDATE_DATA = (0x0011);
	// public static final short SERVER_CMD_REQ_ROLEUPDATE_DATA_LEN 0

	public static final short HALO_CMD_ACK_ROLEUPDATE_DATA_ACCEPT = (0x0012);
	public static final short HALO_CMD_ACK_ROLEUPDATE_DATA_ACCEPT_LEN = 4;

	public static final short SERVER_CMD_ACK_ROLEUPDATE_END = (0x0013);
	public static final short SERVER_CMD_ACK_ROLEUPDATE_END_LEN = 2;

	public static final short HALO_CMD_ACK_DATA_RECOVER = (0x0014);
	public static final short HALO_CMD_ACK_DATA_RECOVER_LEN = 16; 

	public static final short SERVER_CMD_REQ_DATARECOVER = (0x0015);
	public static final short SERVER_CMD_REQ_DATARECOVER_LEN = 44;

	public static final short HALO_CMD_ACK_DATA_RECOVER_ACK = (0x0016);
	public static final short HALO_CMD_ACK_DATA_RECOVER_ACK_LEN = 2;
	
	public static final short HALO_CMD_ACK_DEVICEID_WRITE = (0x0018);
	public static final short HALO_CMD_ACK_DEVICEID_WRITE_LEN = 0;

	public static final short SERVER_CMD_REQ_DEVICEID_WRITE = (0x0019);
	public static final short SERVER_CMD_REQ_DEVICEID_WRITE_LEN = 4;

	public static final short HALO_CMD_ACK_DEVICEID_WRITE_FEEDBACK = (0x001a);
	public static final short HALO_CMD_ACK_DEVICEID_WRITE_FEEDBACK_LEN = 2;
	
	public static final short HALO_CMD_ACK_OTHERSETTING_WRITE = (0x0020);
	public static final short HALO_CMD_ACK_OTHERSETTING_WRITE_LEN = 18;
	
	public static final short SERVER_CMD_REQ_OTHERSETTING_WRITE = (0x0021);
	public static final short SERVER_CMD_REQ_OTHERSETTING_WRITE_LEN = 48;
	
	public static final short HALO_CMD_ACK_OTHERSETTING_ACK = (0x0022);
	public static final short HALO_CMD_ACK_OTHERSETTING_ACK_LEN = 2;

	public static final short SERVER_ACK_ACCOUNT_INVALID = (0x0023);
	public static final short SERVER_ACK_ACCOUNT_INVALID_LEN = 0;

	public static final short SERVER_ACK_NORMAL = (0x0024);
	public static final short SERVER_ACK_NORMAL_LEN = 42;

	public static final short HALO_MAKEFRIEND_REQ = (0x0025);
	public static final short HALO_MAKEFRIEND_REQ_LEN = 8;

	public static final short SERVER_ACK_MAKEFRIEND_REQ = (0x0026);
	public static final short SERVER_ACK_MAKEFRIEND_REQ_LEN = 2;
	
	public static final short SERVER_ACK_INVALID = (short) (0xFFFF);
	
}
