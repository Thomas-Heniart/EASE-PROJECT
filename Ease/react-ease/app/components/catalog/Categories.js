import React, {Component} from 'react';
import {connect} from "react-redux";
import {reduxActionBinder} from "../../actions/index";
import { List, Loader } from 'semantic-ui-react';
import { NavLink } from 'react-router-dom';
import {withRouter} from "react-router-dom";

@connect(store => ({
  categories: store.catalog.categories
}), reduxActionBinder)
class Categories extends Component {
  constructor(props){
    super(props);
  }
  render(){
    return (
        <List link className="listCategory">
          {this.props.categories.length > 0 &&
          <List.Item as={NavLink} exact to={`/main/catalog`} activeClassName="active">
            All
          </List.Item>}
          {this.props.categories.length > 0 && this.props.categories.map(item => {
            return (
                <List.Item key={item.id} as={NavLink} to={`/main/catalog/${item.id}`} activeClassName="active">
                  {item.name}
                </List.Item>
            )
          })}
        </List>
    )
  }
}

export default withRouter(Categories);