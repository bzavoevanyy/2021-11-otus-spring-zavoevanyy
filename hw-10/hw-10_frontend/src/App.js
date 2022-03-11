import './App.css';
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap/dist/js/bootstrap';
import React, {Component} from "react";
import {Header} from "./components/Header";
import {Book} from "./components/Book";
import {Author} from "./components/Author";
import {Genre} from "./components/Genre";
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import BookDetails from "./components/BookDetails";
class App extends Component {

    render() {
        return (
            <Router>
                <div className={"container-fluid"}>
                    <Header/>
                    <main>
                        <Routes>
                            <Route path={"/"}/>
                            <Route path={"/book"} element={<Book/>}/>
                            <Route path={"/book/:id"} element={<BookDetails/>}/>
                            <Route path={"/author"} element={<Author/>}/>
                            <Route path={"/genre"} element={<Genre/>}/>
                        </Routes>
                    </main>
                </div>
            </Router>
        )
    }
}
export default App;
