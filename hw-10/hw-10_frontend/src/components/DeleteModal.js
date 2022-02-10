import React, {useEffect, useState} from "react";
import {deleteAuthor} from "../services/AuthorService";
import {deleteGenre} from "../services/GenreService";
import {deleteBook} from "../services/BookService";
import {useNavigate} from "react-router-dom";
const DeleteModal = (props) => {
    const [entity, setEntity] = useState('')
    const [entityId, setEntityId] = useState('')
    useEffect(() => {
        setEntity(props.entity)
        setEntityId(props.entityId)
    }, [props.entity, props.entityId])

    const navigate = useNavigate()

    const handleClick = (event) => {
        event.preventDefault()
        switch (entity) {
            case "author" : {
                deleteAuthor(entityId).then(data => {
                    console.log(data)
                    removeModals()
                    props.onUpdate()
                })
                break
            }
            case "genre" : {
                deleteGenre(entityId).then(data => {
                    console.log(data)
                    removeModals()
                    props.onUpdate()
                })
                break
            }
            case "book" : {
                deleteBook(entityId).then(data => {
                    console.log(data)
                    removeModals()
                    props.onUpdate(true)
                    navigate("/book")
                })
                break
            }
            default : {
            }
        }

    }
    const removeModals = () => {
        document.querySelectorAll('[data-bs-dismiss=modal]')
            .forEach(elem => elem.click())
    }
    return (
        <div className="modal fade" tabIndex="-1" id="deleteModal">
            <div className="modal-dialog">
                <div className="modal-content">
                    <div className="modal-header">
                        <h5 className="modal-title">Confirmation</h5>
                        <button type="button" className="btn-close" data-bs-dismiss="modal"
                                aria-label="Close"/>
                    </div>
                    <div className="modal-body">
                        <p>Delete {entity}?</p>
                    </div>
                    <div className="modal-footer">
                        <button type="button" className="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <form id="deleteForm">
                            <button onClick={handleClick} type="submit" className="btn btn-primary">Confirm
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    )
}
export default DeleteModal