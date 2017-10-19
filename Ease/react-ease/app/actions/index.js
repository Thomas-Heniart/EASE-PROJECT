import {bindActionCreators} from "redux";
import * as catalogActions from "./catalogActions";

export const ActionCreators = Object.assign({},
    catalogActions
);

export function reduxActionBinder(dispatch) {
  const boundActionCreators = bindActionCreators(ActionCreators, dispatch);
  return {...boundActionCreators, dispatch};
}
