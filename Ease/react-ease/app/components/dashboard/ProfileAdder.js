import React, {Component} from "react";
import { findDOMNode } from 'react-dom';
import classnames from "classnames";
import {Loader, Input, Label,Icon} from 'semantic-ui-react';
import { DropTarget, DragSource } from 'react-dnd';
import {ItemTypes} from "./ItemTypes";
import {insertAppInProfile, createProfile, createProfileAndInsertApp} from "../../actions/dashboardActions";
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
  },
  drop(props, monitor, component){
    const draggedAppProps = monitor.getItem();
    const {column_idx} = props;

    return {
      newProfile: true,
      column_idx: column_idx,
      name: 'New Group',
      app_id: draggedAppProps.app.id
    }
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