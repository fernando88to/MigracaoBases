package mysql

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class EscolaController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Escola.list(params), model:[escolaCount: Escola.count()]
    }

    def show(Escola escola) {
        respond escola
    }

    def create() {
        respond new Escola(params)
    }

    @Transactional
    def save(Escola escola) {
        if (escola == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (escola.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond escola.errors, view:'create'
            return
        }

        escola.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'escola.label', default: 'Escola'), escola.id])
                redirect escola
            }
            '*' { respond escola, [status: CREATED] }
        }
    }

    def edit(Escola escola) {
        respond escola
    }

    @Transactional
    def update(Escola escola) {
        if (escola == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (escola.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond escola.errors, view:'edit'
            return
        }

        escola.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'escola.label', default: 'Escola'), escola.id])
                redirect escola
            }
            '*'{ respond escola, [status: OK] }
        }
    }

    @Transactional
    def delete(Escola escola) {

        if (escola == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        escola.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'escola.label', default: 'Escola'), escola.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'escola.label', default: 'Escola'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
