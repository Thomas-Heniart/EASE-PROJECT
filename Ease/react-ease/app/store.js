import {applyMiddleware, createStore, combineReducers} from "redux"
import {createLogger} from "redux-logger"
import thunk from "redux-thunk"
import promise from "redux-promise-middleware"
import reducers from "./reducers"

const middleware = applyMiddleware(promise(), thunk, createLogger());

export default createStore(reducers, middleware);