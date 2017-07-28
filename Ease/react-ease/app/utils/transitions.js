import React from 'react';
import ReactCSSTransitionGroup from "react-addons-css-transition-group";

export function LeftRightTransition(props){
  return (
      <ReactCSSTransitionGroup
          transitionName="slideLeft"
          transitionAppear={props.appear}
          transitionAppearTimeout={200}
          transitionEnterTimeout={200}
          transitionLeaveTimeout={200}>
        {props.children}
        </ReactCSSTransitionGroup>
  )
}

export function OpacityTransition(props){
  return (
      <ReactCSSTransitionGroup
          transitionName="opacityAnim"
          transitionAppear={props.appear}
          transitionAppearTimeout={200}
          transitionEnterTimeout={200}
          transitionLeaveTimeout={200}>
        {props.children}
      </ReactCSSTransitionGroup>
  )
}