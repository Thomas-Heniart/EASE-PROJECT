import React, {Component} from 'react';

export default function (props) {
  return (
      <div class="ease_modal">
        <div class="modal-background"/>
        <a id="ease_modal_close_btn" class="ease_modal_btn" onClick={props.onClose}>
          <i class="ease_icon fa fa-times"/>
          <span class="key_label">close</span>
        </a>
        <div class="modal_contents_container">
          <div class="contents">
            {props.children}
          </div>
        </div>
      </div>
  )
}