import React from 'react';
import Categories from "./Categories";
import WebsitesContainer from "./WebsitesContainer";
import AddBookmark from './AddBookmark';
import {handleSemanticInput} from "../../utils/utils";
import { Sticky, Rail, Input, List, Button, Icon, Grid, Image, Segment, Checkbox, Form } from 'semantic-ui-react';
import {reduxActionBinder} from "../../actions/index";
import {connect} from "react-redux";
import { NavLink } from 'react-router-dom';
import { Switch, Route } from 'react-router-dom';

@connect(store => ({
  catalog: store.catalog
}), reduxActionBinder)
class Catalog extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      query: '',
      mounted: false
    }
  }
  handleInput = handleSemanticInput.bind(this);
  componentWillMount() {
    document.title = "Apps Catalogue"
  }
  componentDidMount() {
    if (!this.props.catalog.loaded)
      this.props.fetchCatalog();
    setTimeout(() => {
      this.setState({mounted: true});
    }, 1);
  }
  resetQuery = () => {
    this.setState({query: ''});
    this.main_container.scrollTo(0,0);
  };
  render() {
    return (
        <div id="catalog">
          <header>
            <div className="container">
              <div>
                <p>Apps Catalogue</p>
                <Input
                    className="inputSearch centered"
                    placeholder='Search'
                    name="query"
                    onChange={this.handleInput}
                    value={this.state.query} />
              </div>
            </div>
          </header>
          <div class="mainContainer" ref={(ref) => {this.main_container = ref}}>
            <div className="container">
              <Grid>
                <Grid.Column width={3}>
                  <div id="catalog-nav">
                    <Button as={NavLink} to={`/main/catalog/bookmark`} className="bookmarkButton">
                      <Icon name="bookmark" />
                      Add a Bookmark
                    </Button>
                    <Button className="importButton" color="facebook">
                      <Icon name="facebook" />
                      Import Accounts
                    </Button>
                    <Categories resetQuery={this.resetQuery}/>
                  </div>
                </Grid.Column>
                <Grid.Column width={10} style={{marginTop: '23px'}}>
                  {this.state.mounted &&
                    <Switch>
                      <Route path={`${this.props.match.path}/bookmark`} component={AddBookmark}/>
                      <Route path={`${this.props.match.path}`}
                             render={(props) => <WebsitesContainer {...props} query={this.state.query}/>}/>
                    </Switch>}
                </Grid.Column>
                <Grid.Column width={3} />
              </Grid>
            </div>
          </div>
        </div>
    )
  }
}

export default Catalog;