import React, {Component} from "react";
import { findDOMNode } from 'react-dom';
import classnames from "classnames";
import {Input, Icon} from 'semantic-ui-react';
import { DropTarget, DragSource } from 'react-dnd';
import {ItemTypes} from "./ItemTypes";
import {handleSemanticInput} from "../../utils/utils";
import {editProfile, moveProfile, insertAppInProfile, beginProfileDrag,endProfileDrag} from "../../actions/dashboardActions";
import AppWrapper from "./AppWrapper";
import flow from 'lodash/flow';
import {connect} from "react-redux";

class TeamProfile extends Component {
  constructor(props){
    super(props);
  }
  render(){
    const {profile,
      teams_number,
      team_name,
      dashboard,
      connectAppDropTarget,
      connectDropTarget,
      connectDragSource,
      connectDragPreview} = this.props;
    const {dragging_app_id, dragging_profile_id} = this.props.dashboard_dnd;
    const isDragging = profile.id === dragging_profile_id;

    return connectDropTarget(connectAppDropTarget(connectDragPreview(
        <div class={classnames('app_group', isDragging ? 'dragging':null)}>
          {connectDragSource(
              <div>
                <Icon name="move"
                      title="Move"
                      link
                      fitted
                      class="move_button"/>
              </div>
          )}
          {teams_number > 1 &&
          <div class="app_group_type_indicator">
            <Icon name="users"/>
            {team_name}
          </div>}
          <div class="app_group_name">
            #{profile.name}
          </div>
          <div class="apps_container">
            {profile.app_ids.map(id => {
              return (
                  <AppWrapper isDragging={id === dragging_app_id} app={dashboard.apps[id]} key={id}/>
              )})}
          </div>
        </div>
    )))
  }
}

const appTarget = {
  hover(props, monitor, component) {
    const draggedAppProps = monitor.getItem();
    const app = draggedAppProps.app;
    const profile = props.profile;

    if (app.profile_id !== profile.id)
      return;
    props.dispatch(insertAppInProfile({
      app_id: draggedAppProps.app.id,
      profile_id: props.profile.id
    }));
  }
};

const profileSource = {
  beginDrag(props) {
    props.dispatch(beginProfileDrag({profile_id: props.profile.id}));
    return props;
  },
  endDrag(props, monitor) {
    props.dispatch(endProfileDrag({profile_id: props.profile.id}));
  }
};

const profileTarget = {
  hover(props, monitor, component){
    const draggedProfileProps = monitor.getItem();
    if (draggedProfileProps.profile.id === props.profile.id)
      return;

    const hoverBoundingRect = findDOMNode(component).getBoundingClientRect();

    // Get vertical middle
    const hoverMiddleY = (hoverBoundingRect.bottom - hoverBoundingRect.top) / 2;

    // Determine mouse position
    const clientOffset = monitor.getClientOffset();

    // Get pixels to the top
    const hoverClientY = clientOffset.y - hoverBoundingRect.top;

    // Only perform the move when the mouse has crossed half of the items height
    // When dragging downwards, only move when the cursor is below 50%
    // When dragging upwards, only move when the cursor is above 50%

    // Dragging downwards
    props.dispatch(moveProfile({
      profile_id: draggedProfileProps.profile.id,
      targetProfile_id: props.profile.id,
      hoverClientY: hoverClientY,
      hoverMiddleY: hoverMiddleY
    }));
  }
};

export default flow(
    DropTarget(ItemTypes.APP, appTarget, connect => ({
      connectAppDropTarget: connect.dropTarget()
    })),
    DragSource(ItemTypes.PROFILE, profileSource, connect => ({
      connectDragSource: connect.dragSource(),
      connectDragPreview: connect.dragPreview()
    })),
    DropTarget(ItemTypes.PROFILE, profileTarget, connect => ({
      connectDropTarget: connect.dropTarget()
    })),
    connect((store, ownProps) => ({
      dashboard: store.dashboard,
      dashboard_dnd: store.dashboard_dnd,
      team_name: store.teams[ownProps.profile.team_id].name
    }))
)(TeamProfile);