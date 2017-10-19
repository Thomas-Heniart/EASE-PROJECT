import {combineReducers} from "redux";
import * as common from "./commonReducer";
import * as apps from "./appsReducer";
import nav from "./navReducer";

const reducers = Object.assign(
    common,
    apps
);

export default combineReducers({
  ...reducers,
  nav: nav
})
