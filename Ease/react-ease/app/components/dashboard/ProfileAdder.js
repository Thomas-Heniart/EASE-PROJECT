import React, {Component} from "react";
import { findDOMNode } from 'react-dom';
import classnames from "classnames";
import {Loader, Input, Label,Icon} from 'semantic-ui-react';
import { DropTarget, DragSource } from 'react-dnd';
import {ItemTypes} from "./ItemTypes";
import {moveProfile, insertAppInProfile, beginProfileDrag,endProfileDrag} from "../../actions/dashboardActions";
import App from "./App";
import flow from 'lodash/flow';
import {connect} from "react-redux";

class ProfileAdder extends Component {
  constructor(props){
    super(props);
  }
  render(){
    return (
        <div class='app_group_adder'>
          <span>+</span>
          <span>New group</span>
        </div>
    )
  }
}

export default flow(
    connect(store => ({
      dashboard: store.dashboard,
      dashboard_dnd: store.dashboard_dnd
    }))
)(ProfileAdder);