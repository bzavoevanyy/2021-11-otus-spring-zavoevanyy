import React, {Component} from "react";
import {createAuthor, updateAuthor} from "../services/AuthorService";

export class EditAuthor extends Component {

    constructor(props, context) {
        super(props, context);
        this.state = {
            author: {
                authorId: '',
                authorName: ''
            }
        }
    }

    componentWillReceiveProps(nextProps, nextContext) {
        this.setState({author: nextProps.author})
    }

    handleChange = (event) => {
        const name = event.target.name
        this.setState((prevState) => ({
            author: {
                ...prevState.author,
                [name]: event.target.value
            }
        }))
    }
    handleSubmit = (event) => {
        event.preventDefault()
        const authorName = this.state.author.authorName
        if (authorName.length >= 2) {
            if (this.state.author.id) {
                this.updateAuthor()
            } else {
                this.createAuthor()
            }
            document.querySelectorAll('[data-bs-dismiss=modal]')
                .forEach(elem => elem.click())
        }
    }
    updateAuthor = () => {
        const author = this.state.author
        updateAuthor(author)
            .then(response => {
                this.setState({author: response})
                this.props.onAuthorsUpdate()
            })
    }
    createAuthor = () => {
        const author = this.state.author
        createAuthor(author)
            .then(response => {
                this.setState({author: response})
                this.props.onAuthorsUpdate()
            })
    }

    render() {

        return (
            <div className="modal fade" id="editAuthorModal" tabIndex="-1" aria-labelledby="authorModalLabel"
                 aria-hidden="true">
                <div className="modal-dialog">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h5 className="modal-title" id="authorModalLabel">Edit author</h5>
                            <button type="button" className="btn-close" data-bs-dismiss="modal"
                                    aria-label="Close"/>
                        </div>
                        <div className="modal-body">
                            <form id="authorData" method="post" onSubmit={this.handleSubmit}>
                                <div className="mb-3">
                                    <label htmlFor="inputAuthorId1" className="form-label">Author Id</label>
                                    <input type="number" className="form-control"
                                           id="inputAuthorId1" value={this.state.author.authorId || ''} disabled
                                           onChange={this.handleChange}/>
                                </div>
                                <div className="mb-3">
                                    <label htmlFor="authorName1" className="form-label">Author Name</label>
                                    <input name="authorName" type="text" className="form-control"
                                           id="authorName1" value={this.state.author.authorName || ''}
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