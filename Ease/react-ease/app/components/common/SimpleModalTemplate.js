import React, {Component} from 'react';
import {Header, Icon, Transition} from 'semantic-ui-react';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';

export default function (props) {
  const {onClose, headerContent, children, id} = props;
  return (
      <div class="popupHandler myshow">
        <Transition
            visible={true}
            animation='fade'
            duration={300}
            transitionOnMount={true}>
          <div class="popover_mask" onClick={onClose}/>
        </Transition>
        <ReactCSSTransitionGroup
            transitionName="ease-modal"
            transitionAppear={true}
            transitionEnterTimeout={0}
            transitionLeaveTimeout={0}
            transitionAppearTimeout={295}>
          <div class="ease_popup ease_team_popup" id={id}>
            {!!onClose &&
            <button class="button-unstyle action_button close_button" onClick={props.onClose}>
              <Icon name="close" class="mrgn0" link/>
            </button>}
            <Header as="h3" attached="top">
              {headerContent}
            </Header>
            {children}
          </div>
        </ReactCSSTransitionGroup>
      </div>
  )
}