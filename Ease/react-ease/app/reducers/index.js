import {combineReducers} from "redux"

import users from "./usersReducer"
import team from "./teamReducer"
import channels from "./channelsReducer"
import selection from "./selectionReducer"
import teamModals from "./teamModalsReducer"

export default combineReducers({
  team,
  channels,
  users,
  selection,
  teamModals
})
