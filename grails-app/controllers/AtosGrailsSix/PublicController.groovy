package AtosGrailsSix

class PublicController {

    def springSecurityService

    def index() {
        [
                message: "Public access",
                principal: springSecurityService.principal,
        ]
    }

}
