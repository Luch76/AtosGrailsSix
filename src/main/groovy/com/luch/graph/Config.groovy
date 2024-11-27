package com.luch.graph

class Config {
    public static final String AUTHORITY = "https://login.microsoftonline.com/8e1f1398-0cac-4829-936c-f0a1f91e0295";
    public static final String TENANT = "8e1f1398-0cac-4829-936c-f0a1f91e0295";
    public static final String CLIENT_ID = "d52b2177-dbe3-4744-a029-ac75d520e613";
    public static final String SECRET = "g6g8Q~XokY7H9qCtIJWvBfs2wAJDpCuhnYvCDdBx";
    public static final String SCOPES = "User.Read.All"; // Group.Read.All";
    //public static final String SIGN_OUT_ENDPOINT = Config.getProperty("aad.signOutEndpoint");
    //public static final String POST_SIGN_OUT_FRAGMENT = Config.getProperty("aad.postSignOutFragment");
    //public static final Long STATE_TTL = Long.parseLong(Config.getProperty("app.stateTTL"));
    //public static final String HOME_PAGE = Config.getProperty("app.homePage");
    //public static final String REDIRECT_ENDPOINT = "http://localhost:8090/redirect";
    public static final String REDIRECT_URI = "http://localhost:8090/AtosGrailsSix/MicrosoftGraph/codeRedirect";
    //public static final String SESSION_PARAM = Config.getProperty("app.sessionParam");
    //public static final String PROTECTED_ENDPOINTS = Config.getProperty("app.protect.authenticated");
    //public static final String ROLES_PROTECTED_ENDPOINTS = Config.getProperty("app.protect.roles");
    //public static final String ROLE_NAMES_AND_IDS = Config.getProperty("app.roles");
    //public static final String GROUPS_PROTECTED_ENDPOINTS = Config.getProperty("app.protect.groups");
    //public static final String GROUP_NAMES_AND_IDS = Config.getProperty("app.groups");
}
