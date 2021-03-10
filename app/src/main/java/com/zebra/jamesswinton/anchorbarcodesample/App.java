package com.zebra.jamesswinton.anchorbarcodesample;

import android.app.Application;

public class App extends Application {

    // Constants
    public static final String DW_BATCH_PROFILE_NAME = "DataWedgeBatch";
    public static final String DW_BATCH_PROFILE_XML =
            "<wap-provisioningdoc>\n" +
            "  <characteristic type=\"Profile\">\n" +
            "    <parm name=\"ProfileName\" value=\"DataWedgeBatch\"/>\n" +
            "    <characteristic version=\"9.2\" type=\"Batch\">\n" +
            "      <parm name=\"BatchAction\" value=\"1\" />\n" +
            "      <parm name=\"BatchFileType\" value=\"1\" />\n" +
            "      <parm name=\"BatchFileAccessMethod\" value=\"3\" />\n" +
            "      <characteristic type=\"file-details\">\n" +
            "          <parm name=\"XmlFileData\" value=\"77u/PHdhcC1wcm92aXNpb25pbmdkb2M+DQogIDxjaGFyYWN0ZXJpc3RpYyB2ZXJzaW9uPSIxMC4wIiB0eXBlPSJEYXRhV2VkZ2VNZ3IiPg0KICAgIDxwYXJtIG5hbWU9Ik5HU2ltdWxTY2FuVGVtcGxhdGVzIiB2YWx1ZT0iMSIgLz4NCiAgICA8cGFybSBuYW1lPSJOR1NpbXVsU2NhblRlbXBsYXRlRmlsZSIgdmFsdWU9Ii9zZGNhcmQvRG93bmxvYWQvV2hvbGVQYWdlLU1hbmRhdG9yeUJhcmNvZGUueG1sIiAvPg0KICA8L2NoYXJhY3RlcmlzdGljPg0KICA8Y2hhcmFjdGVyaXN0aWMgdmVyc2lvbj0iMTAuMCIgdHlwZT0iRGF0YVdlZGdlTWdyIj4NCiAgICA8cGFybSBuYW1lPSJOR1NpbXVsU2NhblRlbXBsYXRlcyIgdmFsdWU9IjEiIC8+DQogICAgPHBhcm0gbmFtZT0iTkdTaW11bFNjYW5UZW1wbGF0ZUZpbGUiIHZhbHVlPSIvc2RjYXJkL0Rvd25sb2FkL1dob2xlUGFnZS1PcHRpb25hbEJhcmNvZGUueG1sIiAvPg0KICA8L2NoYXJhY3RlcmlzdGljPg0KPC93YXAtcHJvdmlzaW9uaW5nZG9jPg==\" />\n" +
            "      </characteristic>\n" +
            "    </characteristic>\n" +
            "  </characteristic>\n" +
            "</wap-provisioningdoc>";
}
