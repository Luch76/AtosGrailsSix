package AtosGrailsSix

class SecureController {

    def springSecurityService

    def index() {
        respond(
        [
                message: "Access granted to this secure-access-only controller",
                principal: springSecurityService.principal,
        ]
        );
    }

}
