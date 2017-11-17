import React, {Component} from "react";
import classnames from "classnames";
import {Loader, Input, Label,Icon} from 'semantic-ui-react';
import {showLogWithAppSettingsModal, showLinkAppSettingsModal, showSimpleAppSettingsModal, showExtensionDownloadModal} from "../../actions/modalActions";
import Profile from "./Profile";
import DashboardColumn from "./DashboardColumn";
import {connect} from "react-redux";
import HTML5Backend from 'react-dnd-html5-backend';
import { DragDropContext } from 'react-dnd';
import CustomDragLayer from "./CustomDragLayer";
import { DropTarget, DragSource } from 'react-dnd';
import withScrolling from 'react-dnd-scrollzone';

const ScrollingComponent = withScrolling('div');

@connect(store => ({
  dashboard: store.dashboard,
  background_picture: store.common.user.background_picture
}))
class Dashboard extends Component {
  constructor(props){
    super(props);
    this.state = {
      scrolling: false
    }
  }
  onScroll = (e) => {
    if (this.state.scrolling && !e.target.scrollTop){
      this.setState({scrolling: false});
    } else if (!this.state.scrolling && !!e.target.scrollTop){
      this.setState({scrolling: true});
    }
  };
  render(){
    const {columns} = this.props.dashboard;

    return (
        <div id="dashboard" class={classnames(this.props.background_picture ? 'ease-background' : null, this.state.scrolling ? 'scrolling': null, 'lite_scrollbar')}>
          <ScrollingComponent onScroll={this.onScroll} class="ui container fluid full_flex display_flex">
            {columns.map((column,idx) => {
              return (
                  <DashboardColumn idx={idx} key={idx} profile_ids={column}/>
              )
            })}
          </ScrollingComponent>
        </div>
    )
  }
}

export default (Dashboard);