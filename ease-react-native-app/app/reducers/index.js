import {combineReducers} from "redux";
import * as common from "./commonReducer";
import * as apps from "./appsReducer";

export default combineReducers(Object.assign(
    common,
    apps
))
