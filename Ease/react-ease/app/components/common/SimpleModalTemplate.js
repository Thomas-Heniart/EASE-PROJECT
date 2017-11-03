import React, {Component} from 'react';
import {Header, Icon} from 'semantic-ui-react';

export default function (props) {
  const {onClose, headerContent, children} = props;
  return (
      <div class="popupHandler myshow">
        <div class="popover_mask" onClick={onClose}/>
        <div class="ease_popup ease_team_popup">
          {!!onClose &&
          <button class="button-unstyle action_button close_button" onClick={props.onClose}>
            <Icon name="close" class="mrgn0" link/>
          </button>}
          <Header as="h3" attached="top">
            {headerContent}
          </Header>
          {children}
        </div>
      </div>
  )
}