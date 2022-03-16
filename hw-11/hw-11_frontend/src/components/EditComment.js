import React, {Component} from "react";
import {createComment, updateComment} from "../services/CommentService";

export class EditComment extends Component {

    constructor(props, context) {
        super(props, context);
        this.state = {
            comment: {
                commentId: '',
                comment: '',
            },
            bookId: this.props.bookId
        }
    }

    componentWillReceiveProps(nextProps, nextContext) {
        this.setState({comment: nextProps.comment})
    }

    handleChange = (event) => {
        const name = event.target.name
        this.setState((prevState) => ({
            comment: {
                ...prevState.comment,
                [name]: event.target.value
            }
        }))
    }
    handleSubmit = (event) => {
        event.preventDefault()
        const comment = this.state.comment.comment
        if (comment.length >= 2) {
            if (this.state.comment.id) {
                this.updateComment()
            } else {
                this.createComment()
            }
            document.querySelectorAll('[data-bs-dismiss=modal]')
                .forEach(elem => elem.click())
        }
    }
    updateComment = () => {
        const comment = this.state.comment
        comment.bookId = this.props.bookId
        updateComment(comment)
            .then(response => {
                this.setState({comment: response})
                this.props.onUpdate()
            })
    }
    createComment = () => {
        const comment = this.state.comment
        comment.bookId = this.props.bookId
        createComment(comment)
            .then(response => {
                this.setState({comment: response})
                this.props.onUpdate()
            })
    }

    render() {

        return (
            <div className="modal fade" id="editCommentModal" tabIndex="-1" aria-labelledby="commentModalLabel"
                 aria-hidden="true">
                <div className="modal-dialog">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h5 className="modal-title" id="commentModalLabel">Edit comment</h5>
                            <button type="button" className="btn-close" data-bs-dismiss="modal"
                                    aria-label="Close"/>
                        </div>
                        <div className="modal-body">
                            <form id="commentData" method="post" onSubmit={this.handleSubmit}>
                                <div className="mb-3">
                                    <label htmlFor="inputCommentId1" className="form-label">Comment Id</label>
                                    <input type="number" className="form-control"
                                           id="inputCommentId1" value={this.state.comment.commentId || ''} disabled
                                           onChange={this.handleChange}/>
                                </div>
                                <div className="mb-3">
                                    <label htmlFor="comment1" className="form-label">Comment</label>
                                    <input name="comment" type="text" className="form-control"
                                           id="comment1" value={this.state.comment.comment || ''}
                                           onChange={this.handleChange}
                                           required/>
                                </div>
                                <div className="modal-footer">
                                    <button type="button" className="btn btn-secondary" data-bs-dismiss="modal">Close
                                    </button>
                                    <button type="submit" className="btn btn-primary">Save changes</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}