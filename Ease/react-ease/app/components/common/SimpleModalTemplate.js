import React, {Component} from 'react';
import {Header, Icon} from 'semantic-ui-react';

export default function (props) {
  return (
      <div class="popupHandler myshow">
        <div class="popover_mask" onClick={props.onClose}/>
        <div class="ease_popup ease_team_popup">
          <button class="button-unstyle action_button close_button" onClick={props.onClose}>
            <Icon name="close" class="mrgn0" link/>
          </button>
          <Header as="h3" attached="top">
            {props.headerContent}
          </Header>
          {props.children}
        </div>
      </div>
  )
}