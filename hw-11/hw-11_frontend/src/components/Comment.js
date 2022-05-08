import React, {Component} from "react";
import {getAllCommentsByBookId} from "../services/CommentService";
import {EditComment} from "./EditComment";
import DeleteCommentModal from "./DeleteCommentModal";

export class Comment extends Component {

    constructor(props, context) {
        super(props, context);
        this.state = {
            comments: '',
            chosenComment: '',
            entity: ''
        }
    }

    componentDidMount() {
        this.getAllComments()
    }

    getAllComments = () => {
        getAllCommentsByBookId(this.props.bookId).then(data => {
            this.setState({comments: data})
        })
    }
    onUpdateList = () => {
        this.getAllComments()
    }
    handleClick = (event) => {
        const comments = this.state.comments
        const nameOp = event.target.name
        const chosenComment = comments.find(comment => comment.id === event.target.dataset.commentId)
        this.setState({chosenComment: chosenComment || ''})
        if (nameOp === 'delete') {
            this.setState({entity: 'comment'})
        }
    }

    render() {
        const comments = this.state.comments
        return (
            <div>
                <h3>Список комментариев</h3>
                <table className="table">
                    <thead>
                    <tr>
                        {/*<th scope="col">Comment id</th>*/}
                        <th scope="col">Comment</th>
                        <th scope="col">Comment date</th>
                    </tr>
                    </thead>
                    <tbody>
                    {comments && comments.map((comment) => {
                        return (
                            <tr key={comment.id}>
                                {/*<th scope="row">{comment.commentId}</th>*/}
                                <td>{comment.comment}</td>
                                <td>{comment.commentDate}</td>
                                <td>
                                    <button name="edit" onClick={this.handleClick} type="button"
                                            className="btn btn-primary"
                                            data-bs-toggle="modal" data-comment-id={comment.id}
                                            data-bs-target="#editCommentModal">Edit
                                    </button>
                                    <button name="delete" onClick={this.handleClick} type="button"
                                            className="btn btn-primary"
                                            data-bs-toggle="modal" data-comment-id={comment.id}
                                            data-bs-target="#deleteCommentModal">Delete
                                    </button>
                                </td>
                            </tr>)
                    })}
                    </tbody>
                </table>
                <button name="edit" type="button" className="btn btn-primary btn-lg" data-bs-toggle="modal"
                        data-bs-target="#editCommentModal" data-comment-id="0" onClick={this.handleClick}>
                    Add comment
                </button>
                <EditComment
                    onUpdate={this.onUpdateList}
                    comment={this.state.chosenComment}
                    bookId={this.props.bookId}
                />
                <DeleteCommentModal
                    entity={this.state.entity}
                    entityId={this.state.chosenComment.id}
                    onUpdate={this.onUpdateList}/>
            </div>
        )
    }
}