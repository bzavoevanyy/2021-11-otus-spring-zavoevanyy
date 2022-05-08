import React, {useEffect, useState} from "react";
import {deleteComment} from "../services/CommentService";
const DeleteCommentModal = (props) => {
    const [entity, setEntity] = useState('')
    const [entityId, setEntityId] = useState('')
    useEffect(() => {
        setEntity(props.entity)
        setEntityId(props.entityId)
    }, [props.entity, props.entityId])

    const handleClick = (event) => {
        event.preventDefault()
        switch (entity) {
            case "comment" : {
                deleteComment(entityId).then(data => {
                    console.log(data)
                    props.onUpdate()
                    removeModals()
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
        <div className="modal fade" tabIndex="-1" id="deleteCommentModal">
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
                        <form id="deleteCommentForm">
                            <button onClick={handleClick} type="submit" className="btn btn-primary">Confirm
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    )
}
export default DeleteCommentModal