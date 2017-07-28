import React from 'react';
import classnames from 'classnames';

export function FormInput(props){
  return (
      <div class={classnames("input_field", props.checked ? "checked" : null)}>
        <input value={props.value}
               onChange={props.onChange}
               type={props.type}
               name={props.name}
               className={props.classes}
               placeholder={props.placeholder}
               required={props.required}/>
        {props.checked &&
        <span class="outcome">{props.validation_message}</span>}
      </div>
  )
}