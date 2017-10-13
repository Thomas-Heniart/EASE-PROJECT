import React from 'react';
import { Grid, Image, Icon } from 'semantic-ui-react';
import { render } from 'react-router-dom';

class ShowGrid extends React.Component {

    constructor(props){
        super(props);
    }

    render() {

        let appsSorted = this.props.apps.filter((item) => {
            return item.category.toLowerCase().search(
                this.props.categorySelected.toLowerCase()) !== -1;
        });

        return (
            <Grid columns={4} className="logoCatalog">
                {!appsSorted.length ?
                    <div/>
                    :
                    appsSorted.map((item, key) =>
                            <Grid.Column key={key} className="showSegment">
                                <Image src={item.logo}/>
                                <p>{item.name}</p>
                                <Icon name="add square"/>
                            </Grid.Column>
                        )
                }
            </Grid>
        )
    }
}

export default ShowGrid;