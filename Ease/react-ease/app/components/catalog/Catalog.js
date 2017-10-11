import React from 'react';
import ShowGrid from './ShowGrid';
import ListCategory from './ListCategory';
import { Input, List, Button, Icon, Grid, Image } from 'semantic-ui-react';
import { render } from 'react-router-dom';
import style from '../../../../WebContent/cssMinified.v00017/catalog.css';

class Catalog extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            categorySelected: '',
            categories : [
                {name: 'Chill'},
                {name: 'Events'},
                {name: 'Finance'}
            ],
            apps: [
                {name: 'Instagram', logo: '/resources/websites/Instagram/logo.png', category: 'Chill'},
                {name: 'Spotify', logo: '/resources/websites/Spotify/logo.png', category: 'Events'},
                {name: 'Slack', logo: '/resources/websites/Slack/logo.png', category: 'Finance'},
                {name: '9gag', logo: '/resources/websites/9gag/logo.png', category: 'Chill'},
                {name: 'Outlook', logo: '/resources/websites/Outlook/logo.png', category: 'Finance'},
                {name: 'Youtube', logo: '/resources/websites/Youtube/logo.png', category: 'Events'},
                {name: 'Twitter', logo: '/resources/websites/Twitter/logo.png', category: 'Finance'},
                {name: 'Paypal', logo: '/resources/websites/Paypal/logo.png', category: 'Chill'},
                {name: 'Quora', logo: '/resources/websites/Quora/logo.png', category: 'Events'},
                {name: 'Blablacar', logo: '/resources/websites/BlaBlaCar/logo.png', category: 'Finance'},
                {name: 'Sens Critique', logo: '/resources/websites/SensCritique/logo.png', category: 'Chill'},
                {name: 'MailChimp', logo: '/resources/websites/Mailchimp/logo.png', category: 'Events'}
            ],
            allApps: []
        };
    }

    componentWillMount() {
        this.setState({allApps: this.state.apps});
    }

    search = e => {
        let appsFiltered = this.state.apps.filter((item) => {
            return item.name.toString().toLowerCase().search(
                e.target.value.toString().toLowerCase()) !== -1;
        });
        this.setState({allApps: appsFiltered});
    };

    sortList = (e, selected) => {
        this.setState({categorySelected: selected});
    };

    showAllApps = (e) => {
      this.setState({categorySelected: ''});
    };

    render() {
        return (
            <div id="catalog">
                <header>
                    <div className="container">
                        <div>
                            <p>Catalogue d'Apps</p>
                            <Input
                                className="inputSearch centered"
                                placeholder='Search'
                                onChange={e => this.search(e)} />
                        </div>
                    </div>
                </header>
                <div className="container">
                    <Button className="bookmarkButton">
                        <Icon name="bookmark" />
                        Add a Bookmark
                    </Button>
                    <Button className="importButton" color="facebook">
                        <Icon name="facebook" />
                        Import Accounts
                    </Button>
                    <Grid>
                        <Grid.Column width={3}>
                            <ListCategory categories={this.state.categories} sortList={this.sortList} showAllApps={this.showAllApps} />
                        </Grid.Column>
                        <Grid.Column width={13}>
                            <h3>{this.state.categorySelected}</h3>
                                <ShowGrid apps={this.state.allApps} categorySelected={this.state.categorySelected} />
                        </Grid.Column>
                    </Grid>
                </div>
            </div>
        )
    }
}

export default Catalog;