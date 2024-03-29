import React from 'react';
import PropTypes from 'prop-types';
import { DragLayer } from 'react-dnd';
import {ItemTypes} from "./ItemTypes";

const layerStyles = {
  position: 'fixed',
  pointerEvents: 'none',
  zIndex: 100,
  left: 0,
  top: 0,
  width: '100%',
  height: '100%'
};

const AppPreview = ({img_src}) => {
  return (
      <img src={img_src} class="app_drag_preview"/>
  )
};

function getItemStyles(props) {
  const { currentOffset } = props;
  if (!currentOffset) {
    return {
      display: 'none'
    };
  }

  const { x, y } = currentOffset;
  const transform = `translate(${x}px, ${y}px)`;
  return {
    transform: transform,
    WebkitTransform: transform
  };
}

class CustomDragLayer extends React.Component {
  static propTypes = {
    item: PropTypes.object,
    itemType: PropTypes.string,
    currentOffset: PropTypes.shape({
      x: PropTypes.number.isRequired,
      y: PropTypes.number.isRequired
    }),
    isDragging: PropTypes.bool.isRequired
  };

  renderItem(type, item) {
    switch (type) {
      case ItemTypes.APP:
        return (
            <AppPreview img_src={item.app.logo}/>
        );
    }
  }

  render() {
    const { item, itemType, isDragging } = this.props;
    if (!isDragging) {
      return null;
    }

    return (
        <div style={layerStyles}>
          <div style={getItemStyles(this.props)}>
            {this.renderItem(itemType, item)}
          </div>
        </div>
    );
  }
}

let collect = monitor => {
  return {
    item: monitor.getItem(),
    itemType: monitor.getItemType(),
    currentOffset: monitor.getSourceClientOffset(),
    isDragging: monitor.isDragging()
  };
};

export default DragLayer(collect)(CustomDragLayer);