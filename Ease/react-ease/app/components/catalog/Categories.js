import React, {Component} from 'react';
import {connect} from "react-redux";
import {reduxActionBinder} from "../../actions/index";
import { List } from 'semantic-ui-react';
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
    const {resetQuery} = this.props;
    return (
        <List link className="listCategory">
          {this.props.categories.length > 0 &&
          <List.Item as={NavLink} exact to={`/main/catalog/website`} activeClassName="active" onClick={e => {this.props.location.pathname !== `/main/catalog/website` && resetQuery()}}>
            All Apps
          </List.Item>}
          {this.props.categories.length > 0 && this.props.categories.map(item => {
            return (
                <List.Item key={item.id} as={NavLink} to={`/main/catalog/website/${item.id}`} activeClassName="active" onClick={e => {this.props.location.pathname !== `/main/catalog/website/${item.id}` && resetQuery()}}>
                  {item.name}
                </List.Item>
            )
          })}
        </List>
    )
  }
}

export default withRouter(Categories);