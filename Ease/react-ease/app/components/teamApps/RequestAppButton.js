var React = require('react');

function RequestAppButton(props){
  const buttonAction = props.action;
  return (
      <button class="button-unstyle requestAppButton"
              onClick={buttonAction}>
        <span class="onHover">Ask to access</span>
        <span class="default">This app does not concern you</span>
      </button>
  )
}

module.exports = RequestAppButton;