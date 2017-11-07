import React, {Component} from "react";
import {Loader, Input, Label,Icon} from 'semantic-ui-react';

export const EmptyAppIndicator = (props) => {
  return (
      <div class="empty_app_indicator" {...props}>
        <Icon name="info circle" fitted size="large"/>
      </div>
  )
};