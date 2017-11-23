import React, {Component} from "react";
import { DropTarget, DragSource } from 'react-dnd';
import flow from 'lodash/flow';
import classnames from "classnames";
import {ItemTypes} from "./ItemTypes";
import { getEmptyImage } from 'react-dnd-html5-backend';
import ClassicApp from "./ClassicApp";
import LinkApp from "./LinkApp";
import LogWithApp from "./LogWithApp";
import TeamEnterpriseApp from "./TeamEnterpriseApp";
import TeamSingleApp from "./TeamSingleApp";
import TeamLinkApp from "./TeamLinkApp";
import {createProfileAndInsertApp, moveApp, beginAppDrag,endAppDrag, checkIfProfileEmpty} from "../../actions/dashboardActions";
import {connect} from "react-redux";

class AppWrapper extends Component {
  constructor(props){
    super(props);
  }
  componentDidMount(){
    if (this.props.connectDragPreview) {
      this.props.connectDragPreview(getEmptyImage());
    }
  }
  renderApp = () => {
    const {app, dispatch} = this.props;

    switch (app.type){
      case 'classicApp':
        return <ClassicApp app={app} dispatch={dispatch}/>;
      case 'linkApp':
        return <LinkApp app={app} dispatch={dispatch}/>;
      case 'logWithApp':
        return <LogWithApp app={app} dispatch={dispatch}/>;
      case 'teamLinkApp':
        return <TeamLinkApp app={app} dispatch={dispatch}/>;
      case 'teamSingleApp':
        return <TeamSingleApp app={app} dispatch={dispatch}/>;
      case 'teamEnterpriseApp':
        return <TeamEnterpriseApp app={app} dispatch={dispatch}/>;
      default:
        return null;
    }
  };
  render(){
    const {app, connectDragSource, connectDropTarget, isDragging} = this.props;

    return connectDragSource(connectDropTarget(
        <div class="app_wrapper" class={classnames('app_wrapper', isDragging ? 'dragging': null)}>
          {this.renderApp()}
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
    const result = monitor.getDropResult();
    const app_id = props.app.id;

    if (!!result && result.newProfile){
      props.dispatch(createProfileAndInsertApp({
        column_index: result.column_idx,
        name: result.name,
        app_id: app_id,
        last_profile_id: props.app.profile_id
      }));
    } else
      props.dispatch(endAppDrag());
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
)(AppWrapper);