import React, {Component} from "react";
import { Grid, Image, Icon, Modal, Header, Input, Container, Loader } from 'semantic-ui-react';
import AppsContainer from "./AppsContainer";
import {selectItemFromListById} from "../../utils/helperFunctions";
import {connect} from "react-redux";

@connect(store => ({
  categories: store.catalog.categories
}))
class CategoryAppsContainer extends Component {
  constructor(props){
    super(props);
  }
  render(){
    let {websites} = this.props;
    const categoryId = Number(this.props.match.params.categoryId);
    const category = categoryId ? selectItemFromListById(this.props.categories, categoryId) : null;
    const others = websites.filter(item => {
      return item.category_id !== categoryId;
    });
    websites = websites.filter(item => {
      return item.category_id === categoryId;
    });
    return (
        <Container fluid>
          {category && websites.length > 0 &&
          <AppsContainer title={category.name} websites={websites}/>}
          {others.length > 0 &&
          <AppsContainer title={'Other results'} websites={others}/>}
        </Container>
    )
  }
}

export default CategoryAppsContainer;