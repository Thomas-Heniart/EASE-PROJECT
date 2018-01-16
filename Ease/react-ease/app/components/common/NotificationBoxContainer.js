import React, {Component} from "react";
import {deleteNotificationAction} from "../../actions/notificationBoxActions";
import { Header, Label,List, Search,SearchResult, Container, Divider, Icon, Transition, TextArea, Segment, Checkbox, Form, Input, Select, Dropdown, Button, Message } from 'semantic-ui-react';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import {connect} from "react-redux";

class NotificationBox extends Component {
  constructor(props){
    super(props);
  }
  render(){
    const {notification} = this.props;

    return (
        <div class="notification_box">
          <Icon name="delete" link onClick={() => {this.props.dispatch(deleteNotificationAction({id: notification.id}))}}/>
          <span>
            {notification.text}
          </span>
        </div>
    )
  }
}

@connect(store => ({
  notifications: store.notificationBox.notifications
}))
class NotificationBoxContainer extends Component{
  constructor(props){
    super(props);
  }
  render(){
    const {notifications} = this.props;
    return (
        <ReactCSSTransitionGroup
            as="div"
            id="notification_box_container"
            transitionName="notification-box"
            transitionEnterTimeout={990}
            transitionLeaveTimeout={240}>
          {notifications.map(item => {
            return (
                <NotificationBox
                    key={item.id}
                    notification={item}
                    dispatch={this.props.dispatch}/>
            )
          })}
        </ReactCSSTransitionGroup>
    )
  }
}

export default NotificationBoxContainer;