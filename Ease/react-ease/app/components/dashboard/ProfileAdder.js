import React, {Component} from "react";
import { findDOMNode } from 'react-dom';
import classnames from "classnames";
import {Loader, Input, Label,Icon} from 'semantic-ui-react';
import { DropTarget, DragSource } from 'react-dnd';
import {ItemTypes} from "./ItemTypes";
import {insertAppInProfile, createProfile} from "../../actions/dashboardActions";
import App from "./App";
import flow from 'lodash/flow';
import {connect} from "react-redux";

class ProfileAdder extends Component {
  constructor(props){
    super(props);
  }
  render(){
    const {connectDropTarget, column_idx, isOver} = this.props;

    return connectDropTarget(
        <div class={classnames('app_group_adder', isOver ? 'hovered': null)}>
          <span>+</span>
          <span>New group</span>
        </div>
    )
  }
}

const appTarget = {
  hover(props, monitor, component){
    const draggedAppProps = monitor.getItem();
    console.log(props);
    console.log(draggedAppProps);
  },
  drop(props, monitor, component){
    const draggedAppProps = monitor.getItem();
    const {column_idx} = props;

    draggedAppProps.dispatch(createProfile({
      column_index: column_idx,
      name: 'New Group'
    })).then(profile => {
      draggedAppProps.dispatch(insertAppInProfile({
        app_id: draggedAppProps.app.id,
        profile_id: profile.id
      }));
    });
  }
};

export default flow(
    DropTarget(ItemTypes.APP, appTarget, (connect, monitor) => ({
      connectDropTarget: connect.dropTarget(),
      isOver: monitor.isOver()
    })),
    connect(store => ({
      dashboard: store.dashboard,
      dashboard_dnd: store.dashboard_dnd
    }))
)(ProfileAdder);