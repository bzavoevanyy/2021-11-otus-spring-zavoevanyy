import React, {Component} from "react";
import {getAllAuthor} from "../services/AuthorService";
import {EditAuthor} from "./EditAuthor";
import DeleteModal from "./DeleteModal";

export class Author extends Component {

    constructor(props, context) {
        super(props, context);
        this.state = {
            authors: '',
            chosenAuthor: '',
            entity: ''
        }
    }

    componentDidMount() {
        this.getAllAuthors()
    }

    getAllAuthors = () => {
        getAllAuthor().then(data => {
            this.setState({authors: data})
        })
    }
    onUpdateList = () => {
        this.getAllAuthors()
    }
    handleClick = (event) => {
        const authors = this.state.authors
        const nameOp = event.target.name
        const chosenAuthor = authors.find(author => author.authorId === parseInt(event.target.dataset.authorId))
        this.setState({chosenAuthor: chosenAuthor || ''})
        if (nameOp === 'delete') {
            this.setState({entity: 'author'})
        }
    }

    render() {
        const authors = this.state.authors
        return (
            <div>
                <h3>Список авторов</h3>
                <table className="table">
                    <thead>
                    <tr>
                        <th scope="col">Author id</th>
                        <th scope="col">Author name</th>
                    </tr>
                    </thead>
                    <tbody>
                    {authors && authors.map((author) => {
                        return (
                            <tr key={author.authorId}>
                                <th scope="row">{author.authorId}</th>
                                <td>{author.authorName}</td>
                                <td>
                                    <button name="edit" onClick={this.handleClick} type="button"
                                            className="btn btn-primary"
                                            data-bs-toggle="modal" data-author-id={author.authorId}
                                            data-bs-target="#editAuthorModal">Edit
                                    </button>
                                    <button name="delete" onClick={this.handleClick} type="button"
                                            className="btn btn-primary"
                                            data-bs-toggle="modal" data-author-id={author.authorId}
                                            data-bs-target="#deleteModal">Delete
                                    </button>
                                </td>
                            </tr>)
                    })}
                    </tbody>
                </table>
                <button name="edit" type="button" className="btn btn-primary btn-lg" data-bs-toggle="modal"
                        data-bs-target="#editAuthorModal" data-author-id="0" onClick={this.handleClick}>
                    Add author
                </button>
                <EditAuthor
                    onAuthorsUpdate={this.onUpdateList}
                    author={this.state.chosenAuthor}
                />
                <DeleteModal
                    entity={this.state.entity}
                    entityId={this.state.chosenAuthor.authorId}
                    onUpdate={this.onUpdateList}/>
            </div>
        )
    }
}