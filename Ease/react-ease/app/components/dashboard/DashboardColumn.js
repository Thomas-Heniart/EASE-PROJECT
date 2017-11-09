import React, {Component} from "react";
import {Loader, Input, Label,Icon} from 'semantic-ui-react';
import {showLogWithAppSettingsModal, showLinkAppSettingsModal, showSimpleAppSettingsModal, showExtensionDownloadModal} from "../../actions/modalActions";
import {insertProfileIntoColumn} from "../../actions/dashboardActions";
import Profile from "./Profile";
import ProfileAdder from "./ProfileAdder";
import classnames from "classnames";
import {connect} from "react-redux";
import flow from 'lodash/flow';
import {ItemTypes} from "./ItemTypes";
import HTML5Backend from 'react-dnd-html5-backend';
import { DragDropContext } from 'react-dnd';
import CustomDragLayer from "./CustomDragLayer";
import { DropTarget, DragSource } from 'react-dnd';

@connect(store => ({
  dashboard: store.dashboard,
  dashboard_dnd: store.dashboard_dnd
}))
class DashboardColumn extends Component {
  constructor(props){
    super(props);
  }
  render(){
    const {profile_ids, connectDropTarget, idx} = this.props;
    const {profiles} = this.props.dashboard;
    const {dragging_profile_id, dragging_app_id} = this.props.dashboard_dnd;
    const isFitted = dragging_profile_id === -1 && dragging_app_id === -1 && !profile_ids.length;

    return connectDropTarget(
        <div class={classnames("column display_flex flex_direction_column", isFitted ? 'fitted': null)}>
          {profile_ids.map(id => {
            return (
                <Profile profile={profiles[id]} key={id}/>
            )
          })}
          {dragging_app_id !== -1 &&
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
      column_idx: props.idx
    }));
  }
};

export default flow(
    DropTarget(ItemTypes.PROFILE, profileTarget, connect => ({
      connectDropTarget: connect.dropTarget()
    })),
    connect(store => ({
      dashboard: store.dashboard,
      dashboard_dnd: store.dashboard_dnd
    }))
)(DashboardColumn);