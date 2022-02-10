import React from "react";
import {Link} from "react-router-dom";
export const Header = () => {
    return (
        <header>
            <nav className="navbar navbar-expand-lg navbar-light bg-light">
                <div className="container-fluid">
                    <a className="navbar-brand" href="/">Library</a>
                    <button className="navbar-toggler" type="button" data-bs-toggle="collapse"
                            data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                            aria-expanded="false" aria-label="Toggle navigation">
                        <span className="navbar-toggler-icon"/>
                    </button>
                    <div className="collapse navbar-collapse" id="navbarSupportedContent">
                        <ul className="navbar-nav me-auto mb-2 mb-lg-0">
                            <li className="nav-item">
                                <a className="nav-link active" aria-current="page" href="/">Home</a>
                            </li>
                            <li className="nav-item">
                                <Link to="book" className="nav-link" href="/book">Books</Link>
                            </li>
                            <li className="nav-item">
                                <Link to="author" className="nav-link" href="/author">Authors</Link>
                            </li>
                            <li className="nav-item">
                                <Link to="genre" className="nav-link" href="/genre">Genre</Link>
                            </li>
                        </ul>

                    </div>
                </div>
            </nav>
        </header>
    )
}