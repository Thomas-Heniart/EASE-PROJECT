import {combineReducers} from "redux";

import users from "./usersReducer";
import team from "./teamReducer";
import channels from "./channelsReducer";
import selection from "./selectionReducer";
import teamModals from "./teamModalsReducer";
import teamAppsAddUI from "./teamAppsAddUIReducer";
import common from "./commonReducer";
import notifications from "./notificationsReducer";
import * as catalog from "./catalogReducer";
import * as teams from "./teamsReducer";
import * as modals from "./modalReducer";
import * as dashboard from "./dashboardReducer";
import * as teamCard from "./teamCardReducer";

const reducers = Object.assign(
    catalog,
    teams,
    modals,
    dashboard,
    teamCard
);

export default combineReducers({
  ...reducers,
  team,
  channels,
  users,
  selection,
  teamModals,
  teamAppsAddUI,
  common,
  notifications
})
