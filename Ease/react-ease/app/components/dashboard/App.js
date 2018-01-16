import React, {Component} from "react";
import {Loader, Input, Label,Icon} from 'semantic-ui-react';
import { DropTarget, DragSource } from 'react-dnd';
import flow from 'lodash/flow';
import classnames from "classnames";
import {ItemTypes} from "./ItemTypes";
import { getEmptyImage } from 'react-dnd-html5-backend';
import {moveApp, beginAppDrag,endAppDrag} from "../../actions/dashboardActions";
import {connect} from "react-redux";

class App extends Component {
  constructor(props){
    super(props);
  }
  componentDidMount(){
    if (this.props.connectDragPreview) {
      this.props.connectDragPreview(getEmptyImage());
    }
  }
  render(){
    const {app, connectDragSource, connectDropTarget, isDragging} = this.props;

    return connectDragSource(connectDropTarget(
        <div class={classnames('app display_flex flex_direction_column', isDragging ? 'dragging': null)}>
          <div class="logo_area">
            <div style={{display:'none'}} class="app_notification rounded_label"><Icon name="filter"/></div>
            <div class="logo_handler">
              <img class="logo" src={app.logo}/>
              <button class="settings_button">Settings</button>
            </div>
          </div>
          <span class="app_name overflow-ellipsis">{app.name}</span>
        </div>
    ))
  }
}

const appSource = {
  beginDrag(props) {
    props.dispatch(beginAppDrag({app_id: props.app.id}));
    return props;
  },
  endDrag(props, monitor) {
    props.dispatch(endAppDrag({app_id: props.app.id}));
  }
};

const appTarget = {
  hover(props, monitor, component) {
    const draggedAppProps = monitor.getItem();

    if (draggedAppProps.app.id === props.app.id)
      return;
    props.dispatch(moveApp({
      app_id: draggedAppProps.app.id,
      targetApp_id: props.app.id
    }));
  }
};

export default flow(
    DragSource(ItemTypes.APP, appSource, (connect, monitor) => ({
      connectDragSource: connect.dragSource(),
      connectDragPreview: connect.dragPreview()
    })),
    DropTarget(ItemTypes.APP, appTarget, connect => ({
      connectDropTarget: connect.dropTarget()
    })),
    connect()
)(App);