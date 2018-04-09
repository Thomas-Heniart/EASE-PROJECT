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
import TeamAnyEnterpriseApp from "./TeamAnyEnterpriseApp";
import TeamSoftwareEnterpriseApp from "./TeamSoftwareEnterpriseApp";
import TeamSingleApp from "./TeamSingleApp";
import TeamAnySingleApp from "./TeamAnySingleApp";
import TeamSoftwareSingleApp from "./TeamSoftwareSingleApp";
import TeamLinkApp from "./TeamLinkApp";
import SsoApp from  "./SsoApp";
import AnyApp from "./AnyApp"
import SoftwareApp from "./SoftwareApp";
import {createProfileAndInsertApp, moveApp, beginAppDrag,endAppDrag} from "../../actions/dashboardActions";
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
        if (app.sub_type === 'classic')
          return <TeamSingleApp app={app} dispatch={dispatch}/>;
        else if (app.sub_type === 'any')
          return <TeamAnySingleApp app={app} dispatch={dispatch}/>;
        else
          return <TeamSoftwareSingleApp app={app} dispatch={dispatch}/>;
      case 'teamEnterpriseApp':
        if (app.sub_type === 'classic')
          return <TeamEnterpriseApp app={app} dispatch={dispatch}/>;
        else if (app.sub_type === 'any')
          return <TeamAnyEnterpriseApp app={app} dispatch={dispatch}/>;
        else
          return <TeamSoftwareEnterpriseApp app={app} dispatch={dispatch}/>;
      case 'ssoApp':
        return <SsoApp app={app} dispatch={dispatch}/>;
      case 'anyApp':
        return <AnyApp app={app} dispatch={dispatch}/>;
      case 'softwareApp':
        return <SoftwareApp app={app} dispatch={dispatch}/>;
      default:
        return null;
    }
  };
  render(){
    const {connectDragSource, connectDropTarget, isDragging, app} = this.props;

    return connectDragSource(connectDropTarget(
        <div id={`app_${this.props.app.id}`} class={classnames('app_wrapper',!!app.empty ? 'empty':null, isDragging ? 'dragging': null)}>
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