import React, {Component} from 'react';
import Categories from "./Categories";
import WebsitesContainer from "./WebsitesContainer";
import AddBookmark from './AddBookmark';
import AddAnyApp from './AddAnyApp'
import AddSoftwareCredentials from './AddSoftwareCredentials';
import Importations from './Importations'
import OnBoardingImportation from './OnBoardingImportation';
import {handleSemanticInput} from "../../utils/utils";
import { Grid, Menu, Input, Icon } from 'semantic-ui-react';
import {reduxActionBinder} from "../../actions/index";
import {connect} from "react-redux";
import { NavLink } from 'react-router-dom';
import { Switch, Route } from 'react-router-dom';

@connect(store => ({
  catalog: store.catalog
}), reduxActionBinder)
class Catalog extends Component {
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
    if (this.state.query.length > 0 && this.props.location.pathname === '/main/catalog/website/addWebsite') {
      window.location.href = '/#/main/catalog/website';
    }
    return (
        <div id="catalog" class="bordered_scrollbar">
          <header>
            <div className="container">
              <div>
                <p>Apps Catalogue</p>
                {this.props.location.pathname !== `/main/catalog/onBoardingImportation` &&
                <Menu tabular>
                  <Menu.Item name='Add an App' icon='book' as={NavLink} to={`/main/catalog/website`} activeClassName="active" onClick={e => {this.props.location.pathname !== `/main/catalog/website` && this.resetQuery()}}/>
                  <Menu.Item name='Add a Shortcut link' icon='bookmark' as={NavLink} exact to={`/main/catalog/bookmark`} activeClassName="active" onClick={e => {this.props.location.pathname !== `/main/catalog/bookmark` && this.resetQuery()}}/>
                  <Menu.Item name='Add Software credentials' icon='disk outline' as={NavLink} exact to={`/main/catalog/softwareCredentials`} activeClassName="active" onClick={e => {this.props.location.pathname !== `/main/catalog/softwareCredentials` && this.resetQuery()}}/>
                  <Menu.Item name='Import' icon='cloud upload' as={NavLink} exact to={`/main/catalog/importations`} activeClassName="active" onClick={e => {this.props.location.pathname !== `/main/catalog/importations` && this.resetQuery()}}/>
                </Menu>}
              </div>
            </div>
          </header>
          {/website/.test(this.props.location.pathname) &&
          <div className='container' style={{marginBottom:'30px'}}>
            <Input fluid
                   autoFocus
                   className="inputSearch"
                   placeholder='Search'
                   name="query"
                   onChange={this.handleInput}
                   value={this.state.query} />
            <NavLink style={{color:'#949eb7',marginLeft:'21%'}} to={`/main/catalog/website/addWebsite`} onClick={e => this.setState({query:''})}>If you wish to add a specific website, click here <Icon name='gift'/></NavLink>
          </div>}
          <div class="mainContainer" ref={(ref) => {this.main_container = ref}} style={{paddingTop:'0'}}>
            <div className="container">
              <Grid>
                <Grid.Column width={3} style={{paddingTop:'45px',paddingLeft:'0'}}>
                  {/website/.test(this.props.location.pathname) &&
                  <div id="catalog-nav">
                    <Categories resetQuery={this.resetQuery}/>
                  </div>}
                </Grid.Column>
                <Grid.Column width={10}>
                  {this.state.mounted &&
                    <Switch>
                      <Route path={`${this.props.match.path}/importations`} component={Importations}/>
                      <Route path={`${this.props.match.path}/onBoardingImportation`} component={OnBoardingImportation}/>
                      <Route path={`${this.props.match.path}/softwareCredentials`} component={AddSoftwareCredentials}/>
                      <Route path={`${this.props.match.path}/bookmark`} component={AddBookmark}/>
                      <Route exact path={`${this.props.match.path}/website/addWebsite`}
                             render={(props) => <AddAnyApp cross focus={true}/>}/>
                      <Route path={`${this.props.match.path}/website`}
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