import React, {Component} from "react";
import {Loader, Input, Label,Icon} from 'semantic-ui-react';

export const EmptyAppIndicator = (props) => {
  return (
      <div class="app_overlay_indicator" {...props}>
        <Icon name="exclamation triangle" fitted size="large"/>
      </div>
  )
};

export const WaitingTeamApproveIndicator = (props) => {
  return (
      <div class="app_overlay_indicator" {...props}>
        <Icon name="hourglass half" fitted size="large"/>
      </div>
  )
};

export const EmptyTeamAppIndicator = (props) => {
  return (
      <div class="app_overlay_indicator red" {...props}>
        <Icon name="wrench" fitted size="large"/>
      </div>
  )
};

export const DisabledAppIndicator = (props) => {
  return (
      <div class="app_overlay_indicator grey" {...props}/>
  )
};

export const NewAppLabel = (props) => {
  return (
      <Label circular class="new_app_label">New</Label>
  )
};