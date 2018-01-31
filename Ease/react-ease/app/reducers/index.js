import {combineReducers} from "redux";

import teamModals from "./teamModalsReducer";
import teamAppsAddUI from "./teamAppsAddUIReducer";
import common from "./commonReducer";
import notifications from "./notificationsReducer";
import * as catalog from "./catalogReducer";
import * as teams from "./teamsReducer";
import * as modals from "./modalReducer";
import * as dashboard from "./dashboardReducer";
import * as teamCard from "./teamCardReducer";
import * as notificationBox from "./notificationBoxReducer";
import * as onBoarding from "./onBoarding";

const reducers = Object.assign(
    catalog,
    teams,
    modals,
    dashboard,
    teamCard,
    notificationBox,
    onBoarding
);

export default combineReducers({
  ...reducers,
  teamModals,
  teamAppsAddUI,
  common,
  notifications
})
