import {bindActionCreators} from "redux";
import * as catalogActions from "./catalogActions";
import * as teamCardActions from "./teamCardActions";

export const ActionCreators = Object.assign({},
    catalogActions,
    teamCardActions
);

export function reduxActionBinder(dispatch) {
  const boundActionCreators = bindActionCreators(ActionCreators, dispatch);
  return {...boundActionCreators, dispatch};
}
