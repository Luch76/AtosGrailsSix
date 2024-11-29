package luchgrails

class SecureController {

    def springSecurityService

    def index() {
        respond(
        [
                message: "Secure access only",
                principal: springSecurityService.principal,
        ]
        );
    }

}
