import React from 'react';
import {Tooltip} from 'react-lightweight-tooltip';

const app_receiver_tooltip_style = {
  wrapper: {
    position: 'relative',
    display: 'inline-block',
    zIndex: '0',
    color: '#000',
    cursor: 'normal'
  },
  tooltip: {
    position: 'absolute',
    borderRadius: '5px',
    zIndex: '1',
    background: '#000',
    top: '100%',
    bottom: 'none',
    left: '50%',
    marginTop: '10px',
    padding: '5px',
    WebkitTransform: 'translateX(-50%)',
    msTransform: 'translateX(-50%)',
    OTransform: 'translateX(-50%)',
    transform: 'translateX(-50%)',
  },
  content: {
    background: '#000',
    color: '#fff',
    fontSize: '.8em',
    padding: '.3em 1em',
    whiteSpace: 'nowrap',
  },
  arrow: {
    position: 'absolute',
    width: '0',
    height: '0',
    top: '-5px',
    left: '50%',
    marginLeft: '-5px',
    borderRight: 'solid transparent 10px',
    borderLeft: 'solid transparent 10px',
    borderBottom: 'solid #000 10px',
    borderTop: 'none'
  },
  gap: {
    position: 'absolute',
    width: '100%',
    height: '20px',
    top: '-20px',
  },
};

function AppReceiverTooltip(props){
  return (
      <Tooltip content={props.content} styles={app_receiver_tooltip_style}>
        {props.children}
      </Tooltip>
  )
}

export default AppReceiverTooltip;