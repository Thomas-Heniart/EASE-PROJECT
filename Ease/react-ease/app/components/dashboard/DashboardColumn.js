import React, {Component} from "react";
import {showSimpleAppSettingsModal} from "../../actions/modalActions";
import {insertProfileIntoColumn} from "../../actions/dashboardActions";
import Profile from "./Profile";
import TeamProfile from "./TeamProfile";
import ProfileAdder from "./ProfileAdder";
import classnames from "classnames";
import {connect} from "react-redux";
import flow from 'lodash/flow';
import {ItemTypes} from "./ItemTypes";
import { DropTarget } from 'react-dnd';

@connect(store => ({
  dashboard: store.dashboard,
  dashboard_dnd: store.dashboard_dnd
}))
class DashboardColumn extends Component {
  constructor(props){
    super(props);
  }
  render(){
    const {profile_ids, connectDropTarget, idx, teams_number} = this.props;
    const {profiles, apps} = this.props.dashboard;
    const {dragging_profile_id, dragging_app_id} = this.props.dashboard_dnd;
    const isFitted = dragging_profile_id === -1 && dragging_app_id === -1 && !profile_ids.length;
    return connectDropTarget(
        <div class={classnames("column display_flex flex_direction_column", isFitted ? 'fitted': null)}>
          {profile_ids.map(id => {
            const profile = profiles[id];
            if (profile.team_id !== -1)
              return (
                  <TeamProfile profile={profiles[id]}
                               teams_number={teams_number}
                               key={id}/>
              );
            return (
                <Profile profile={profiles[id]} key={id}/>
            )
          })}
          {dragging_app_id !== -1 && !apps[dragging_app_id].team_id &&
          <ProfileAdder column_idx={idx}/>}
        </div>
    )
  }
}

const profileTarget = {
  hover(props, monitor, component){
    const draggedProfileProps = monitor.getItem();
    props.dispatch(insertProfileIntoColumn({
      profile_id: draggedProfileProps.profile.id,
      column_index: props.idx
    }));
  }
};

export default flow(
    DropTarget(ItemTypes.PROFILE, profileTarget, connect => ({
      connectDropTarget: connect.dropTarget()
    })),
    connect(store => ({
      dashboard: store.dashboard,
      teams_number: Object.keys(store.teams).length,
      dashboard_dnd: store.dashboard_dnd
    }))
)(DashboardColumn);