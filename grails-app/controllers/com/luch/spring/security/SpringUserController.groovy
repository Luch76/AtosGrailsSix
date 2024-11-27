package com.luch.spring.security

import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

@Secured('ROLE_ADMIN')
class SpringUserController {

    SpringUserService springUserService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond springUserService.list(params), model:[springUserCount: springUserService.count()]
    }

    def show(Long id) {
        respond springUserService.get(id)
    }

    def create() {
        respond new SpringUser(params)
    }

    def save(SpringUser springUser) {
        if (springUser == null) {
            notFound()
            return
        }

        try {
            springUserService.save(springUser)
        } catch (ValidationException e) {
            respond springUser.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'springUser.label', default: 'SpringUser'), springUser.id])
                redirect springUser
            }
            '*' { respond springUser, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond springUserService.get(id)
    }

    def update(SpringUser springUser) {
        if (springUser == null) {
            notFound()
            return
        }

        try {
            springUserService.save(springUser)
        } catch (ValidationException e) {
            respond springUser.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'springUser.label', default: 'SpringUser'), springUser.id])
                redirect springUser
            }
            '*'{ respond springUser, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        springUserService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'springUser.label', default: 'SpringUser'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'springUser.label', default: 'SpringUser'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
