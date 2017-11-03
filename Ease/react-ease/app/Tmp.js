import React, {Component} from "react";
import HTML5Backend from 'react-dnd-html5-backend';
import { DragDropContext } from 'react-dnd';
import { DropTarget, DragSource } from 'react-dnd';

const ItemTypes = {
  column: 'column',
  profile: 'profile'
};

const ColumnSource = {
  beginDrag(props){
    return {};
  }
};

class Profile extends Component {
  constructor(props){
    super(props);
  }
  render(){
    return (
        <div style={{height: '150px', marginBottom: '5px',backgroundColor: "#373B60"}}>

        </div>
    )
  }
}


class Column extends Component {
  constructor(props){
    super(props);
  }
  render(){
    return (
        <div class="display_flex flex_direction_column" style={{border: '1px solid black', height: '100%', width: '25%', backgroundColor: "#0A0"}}>
          <Profile/>
          <Profile/>
        </div>
    )
  }
}

class Table extends Component {
  constructor(props){
    super(props);
    this.state = {
      columns_id: [1,2,3,4],
      columns: {
        1: {id : 1},
        2: {id : 2},
        3: {id : 3},
        4: {id : 4}
      }
    }
  }
  render(){
    return (
        <div class="display_flex" style={{width: '400px', height: '400px', padding:'2px', backgroundColor: '#A00'}}>
          {this.state.columns_id.map(id => {
            return (
                <Column column={this.state.columns[id]} key={id}/>
            )})}
        </div>
    )
  }
}

@DragDropContext(HTML5Backend)
class Tmp extends Component {
  constructor(props){
    super(props);
  }
  render(){
    return (
        <Table/>
    )
  }
}

export default Tmp;